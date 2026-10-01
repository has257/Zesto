package com.foodngo.backend.controller;

import com.foodngo.backend.dto.ImpactRequest;
import com.foodngo.backend.dto.ImpactResponse;
import com.foodngo.backend.service.ImpactService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/impact")
public class ImpactController {

    private final ImpactService impactService;

    public ImpactController(ImpactService impactService) {
        this.impactService = impactService;
    }

        @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ImpactResponse createImpact(
            @Valid @RequestBody ImpactRequest request) {

        return impactService.createImpact(request);
    }

    @GetMapping
    public List<ImpactResponse> getAllImpacts() {

        return impactService.getAllImpacts();
    }

    @GetMapping("/{id}")
    public ImpactResponse getImpactById(
            @PathVariable Long id) {

        return impactService.getImpactById(id);
    }

    @PutMapping("/{id}")
    public ImpactResponse updateImpact(
            @PathVariable Long id,
            @Valid @RequestBody ImpactRequest request) {

        return impactService.updateImpact(id, request);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteImpact(@PathVariable Long id) {

        impactService.deleteImpact(id);
    }
}