package com.foodngo.backend.service;

import com.foodngo.backend.entity.Status;
import com.foodngo.backend.repository.StatusRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatusService {

    private final StatusRepository statusRepository;

    public StatusService(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    public List<Status> getAllStatuses() {
        return statusRepository.findAll();
    }

    public Status createStatus(Status status) {
        return statusRepository.save(status);
    }
}