package com.jann.csv_data_hub.controller;

import com.jann.csv_data_hub.message.tracker.dto.RequestTrackerResponse;
import com.jann.csv_data_hub.message.tracker.service.RequestTrackerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/request-tracker")
public class RequestTrackerController {

    private final RequestTrackerService requestTrackerService;

    public RequestTrackerController(RequestTrackerService requestTrackerService) {
        this.requestTrackerService = requestTrackerService;
    }

    @GetMapping
    public ResponseEntity<Page<RequestTrackerResponse>> requestStatus(Pageable pageable) {
        return ResponseEntity.ok(requestTrackerService.getRequestTrackers(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RequestTrackerResponse> requestStatusById(@PathVariable String id) {
        return ResponseEntity.ok(requestTrackerService.getRequestTrackerById(id));
    }
}
