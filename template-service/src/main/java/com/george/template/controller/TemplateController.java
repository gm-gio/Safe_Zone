package com.george.template.controller;

import com.george.template.dto.TemplateRequest;
import com.george.template.dto.TemplateResponse;
import com.george.template.service.TemplateService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/templates")
public class TemplateController {

    private final TemplateService templateService;


    @PostMapping("/new" )
    @Operation(summary = "create new a template" )
    public ResponseEntity<TemplateResponse> create(@RequestBody TemplateRequest templateRequest) {
        return ResponseEntity.status(CREATED).body(templateService.create(templateRequest));
    }

    @GetMapping("/{templateId}" )
    @Operation(summary = "receive a Template  by ID" )
    public ResponseEntity<TemplateResponse> getById(@PathVariable Long templateId) {
        return ResponseEntity.status(OK).body(templateService.getById(templateId));
    }

    @DeleteMapping("/{templateId}" )
    @Operation(summary = "delete a Template by ID" )
    public ResponseEntity<Void> deleteById(@PathVariable Long templateId) {
        templateService.deleteById(templateId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{templateId}" )
    @Operation(summary = "update a Template by ID" )
    public ResponseEntity<TemplateResponse> updateUser(
            @PathVariable Long templateId,
            @RequestBody TemplateRequest templateRequest) {

        return ResponseEntity.status(OK).body(templateService.update(templateId, templateRequest));
    }
}
