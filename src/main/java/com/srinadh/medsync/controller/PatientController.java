package com.srinadh.medsync.controller;

import com.srinadh.medsync.dto.PatientDTO;
import com.srinadh.medsync.entity.Patient;
import com.srinadh.medsync.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientService service;

    public PatientController(PatientService service) {
        this.service = service;
    }

    @PostMapping
    public Patient create(@Valid @RequestBody PatientDTO patientDTO) {
        return service.save(patientDTO);
    }

    @GetMapping
    public Page<Patient> getAll(@RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return service.getPatientsPaginated(pageable);
    }

    @GetMapping("/search")
    public List<Patient> searchByDisease(@RequestParam String disease) {
        return service.getPatientsByDisease(disease);
    }

    @PutMapping("/{id}")
    public Patient updatePatient(@PathVariable Long id, @Valid @RequestBody PatientDTO patientDTO) {
        return service.updatePatient(id, patientDTO);
    }

    @DeleteMapping("/{id}")
    public void deletePatient(@PathVariable Long id) {
        service.deletePatient(id);
    }
}
