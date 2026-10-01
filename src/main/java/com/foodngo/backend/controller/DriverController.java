package com.foodngo.backend.controller;

import com.foodngo.backend.entity.Driver;
import com.foodngo.backend.service.DriverService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drivers")
public class DriverController {

    private final DriverService service;

    public DriverController(DriverService service) {
        this.service = service;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Driver createDriver(@Valid @RequestBody Driver driver) {
        return service.createDriver(driver);
    }

    @GetMapping
    public List<Driver> getAllDrivers() {
        return service.getAllDrivers();
    }

    @GetMapping("/{id}")
    public Driver getDriverById(@PathVariable Long id) {
        return service.getDriverById(id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteDriver(@PathVariable Long id) {
        service.deleteDriver(id);
    }
}