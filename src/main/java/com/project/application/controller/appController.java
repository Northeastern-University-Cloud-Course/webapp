package com.project.application.controller;

import com.project.application.Token;
import com.project.application.User;
import com.project.application.repositories.TokenVerification;
import com.project.application.repositories.userRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.project.application.services.healthService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Optional;


@RestController
@RequestMapping("")
public class appController {

    @Autowired
    healthService healthservice;

    @Autowired
    private TokenVerification tokenrepo;

    @Autowired
    private userRepository userRepo;

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

    @GetMapping("/verify")
    public ResponseEntity<?> verifyuser(HttpServletRequest req,@RequestParam("token") String tok){

        try {
            Optional<Token> t = tokenrepo.findByLink(tok);
            if(t.isEmpty()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            Token tk = t.get();
            Timestamp ts = tk.getExptime();

            long exp_time = ts.getTime();
            Timestamp now = new Timestamp(System.currentTimeMillis());
            long curr_time = now.getTime();

            if(exp_time-curr_time>0){
                tk.setVerified(true);
                return ResponseEntity.status(HttpStatus.OK).body("User Verified !!");
            }
            else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Verification Request timed out. Try again !!");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
        }
    }

}
