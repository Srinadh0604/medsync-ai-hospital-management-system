package com.srinadh.medsync.service;

import org.springframework.stereotype.Service;

@Service
public class CsvService {

    public byte[] generateCsv(String content) {

        return content.getBytes();
    }
}
