package com.project.application.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.project.application.services.healthService;


@RestController
@RequestMapping("")
public class appController {

    @Autowired
    healthService healthservice;

    @GetMapping("/healthz")
    public ResponseEntity<Void> fetch(HttpServletRequest request) {
        if(request.getContentLength()>0 || request.getQueryString()!=null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("cache-control", "no-cache").build();
        }

        return healthservice.health();
    }
    @PostMapping("/healthz")
    public ResponseEntity<Void> post(){
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).header("cache-control", "no-cache").build();
    }
    @PutMapping("/healthz")
    public ResponseEntity<Void> put(){
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).header("cache-control", "no-cache").build();
    }
    @DeleteMapping("/healthz")
    public ResponseEntity<Void> delete(){
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).header("cache-control", "no-cache").build();
    }
    @PatchMapping("/healthz")
    public ResponseEntity<Void> patch(){
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).header("cache-control", "no-cache").build();
    }

}
