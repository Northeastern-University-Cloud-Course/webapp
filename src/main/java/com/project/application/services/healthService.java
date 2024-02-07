package com.project.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;

@Service
public class healthService {

    @Autowired
    DataSource datasource;

    public ResponseEntity<Void> health() {

        try (Connection connection = datasource.getConnection()) {
            return ResponseEntity.status(HttpStatus.OK).header("cache-control", "no-cache").build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).header("cache-control", "no-cache").build();
        }

    }
}
