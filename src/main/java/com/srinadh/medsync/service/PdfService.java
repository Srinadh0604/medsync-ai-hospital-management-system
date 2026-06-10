package com.srinadh.medsync.service;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

@Service
public class PdfService {

    private static final Logger log = LoggerFactory.getLogger(PdfService.class);

    private final TemplateEngine templateEngine;
    private final AuditService auditService;

    public PdfService(TemplateEngine templateEngine, AuditService auditService) {
        this.templateEngine = templateEngine;
        this.auditService = auditService;
    }

    public void generatePdf(String templateName, Map<String, Object> variables, String outputFilePath) {
        Context context = new Context();
        context.setVariables(variables);

        String htmlContent = templateEngine.process(templateName, context);
        convertHtmlToPdf(htmlContent, outputFilePath);
    }

    public byte[] generatePdfBytes(String templateName, Map<String, Object> variables) {
        Context context = new Context();
        context.setVariables(variables);

        log.debug("Rendering template '{}' with variables: {}", templateName, variables.keySet());

        String htmlContent = templateEngine.process(templateName, context);
        String aiSummary = variables.get("aiSummary") != null ? variables.get("aiSummary").toString() : null;

        logThymeleafRender(htmlContent, aiSummary, templateName);

        byte[] pdfBytes = convertHtmlToPdfBytes(htmlContent);
        log.info("PDF generated for template '{}', size={} bytes", templateName, pdfBytes.length);

        auditService.logAction(
                "SYSTEM",
                "GENERATE_PDF");
        return pdfBytes;
    }

    private void logThymeleafRender(String htmlContent, String aiSummary, String templateName) {
        if (aiSummary == null || aiSummary.isBlank()) {
            log.warn("aiSummary variable is null or blank for template '{}'", templateName);
            return;
        }

        boolean rendered = htmlContent.contains(aiSummary);
        log.debug("Thymeleaf render complete for template '{}': aiSummaryInHtml={}", templateName, rendered);
        if (!rendered) {
            log.warn("aiSummary was provided but not found in rendered HTML for template '{}'", templateName);
        }
    }

    public byte[] convertHtmlToPdfBytes(String htmlContent) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            convertHtmlToPdf(htmlContent, outputStream);
            return outputStream.toByteArray();
        } catch (IOException ex) {
            throw new RuntimeException("Failed to convert HTML to PDF", ex);
        }
    }

    public void convertHtmlToPdf(String htmlContent, String outputFilePath) {
        try {
            Path outputPath = Path.of(outputFilePath);
            if (outputPath.getParent() != null) {
                Files.createDirectories(outputPath.getParent());
            }

            try (OutputStream outputStream = new FileOutputStream(outputPath.toFile())) {
                convertHtmlToPdf(htmlContent, outputStream);
            }
        } catch (Exception ex) {
            throw new RuntimeException("Failed to generate PDF file", ex);
        }
    }

    public void convertHtmlToPdf(String htmlContent, OutputStream outputStream) {
        try {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(outputStream);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to convert HTML to PDF", ex);
        }
    }
}
