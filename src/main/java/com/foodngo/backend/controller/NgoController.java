package com.foodngo.backend.controller;

import com.foodngo.backend.dto.NgoRequest;
import com.foodngo.backend.dto.NgoResponse;
import com.foodngo.backend.service.NgoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ngos")
public class NgoController {

    private final NgoService ngoService;

    public NgoController(NgoService ngoService) {
        this.ngoService = ngoService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public NgoResponse createNgo(@Valid @RequestBody NgoRequest request) {
        return ngoService.createNgo(request);
    }

    @GetMapping
    public List<NgoResponse> getAllNgos() {
        return ngoService.getAllNgos();
    }

    @GetMapping("/{id}")
    public NgoResponse getNgoById(@PathVariable Long id) {
        return ngoService.getNgoById(id);
    }

    @DeleteMapping("/{id}")
    public String deleteNgo(@PathVariable Long id) {
        ngoService.deleteNgo(id);
        return "NGO deleted successfully";
    }
}