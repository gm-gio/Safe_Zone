package com.george.fileservice.controller;


import com.george.fileservice.exception.InvalidFileFormatException;
import com.george.fileservice.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/files")
public class FileServiceController {

    private final FileService fileService;


    @PostMapping("/upload-users")
    @Operation(summary = "bulk User registration with provided CSV")
    public ResponseEntity<String> registerUsersFromCsv(

            @RequestPart @Valid @NotNull(message = "{file.csv.not_null}") MultipartFile file
    ) {

        if (!Objects.equals(file.getContentType(), "text/csv")) {
            throw new InvalidFileFormatException("Invalid file format. Only CSV files are allowed.");
        }

        fileService.uploadUsers(file);
        return ResponseEntity.status(HttpStatus.CREATED).body("Users successfully registered.");
    }

    @GetMapping("/generate/csv")
    @Operation(summary = "Generate CSV file with users")
    public ResponseEntity<ByteArrayResource> generateCsvFile() {
        byte[] data = fileService.generateCsv();
        ByteArrayResource resource = new ByteArrayResource(data);
        return ResponseEntity.ok()
                .contentLength(data.length)
                .header("Content-type", "text/csv")
                .header("Content-disposition", "attachment; filename=\"output.csv\"")
                .body(resource);
    }
}
