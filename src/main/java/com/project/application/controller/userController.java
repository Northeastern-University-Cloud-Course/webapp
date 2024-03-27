package com.project.application.controller;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.project.application.Token;
import com.project.application.User;
import com.project.application.repositories.TokenVerification;
import com.project.application.repositories.userRepository;
import com.project.application.services.healthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.pubsub.v1.ProjectTopicName;
import java.io.FileInputStream;
import java.io.IOException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.ProjectTopicName;
import com.google.pubsub.v1.PubsubMessage;
import com.google.gson.Gson;


@RestController
@RequestMapping("/v1")
public class userController {

    @Autowired
    private userRepository userRepo;

    @Autowired
    private TokenVerification tv;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private healthService healthservice;


    @PostMapping("/user")
    public ResponseEntity<?> createuser(HttpServletRequest request, @Validated @RequestBody User ua) {

        try {
            ResponseEntity<Object> build = ResponseEntity.status(HttpStatus.OK).header("cache-control", "no-cache").build();
            if(!healthservice.health().equals(build)){
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
            }

            if(request.getQueryString()!=null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("cache-control", "no-cache").build();
            }

            if(ua.getPassword().isEmpty()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("cache-control", "no-cache").body("Password cannot be empty.");
            }
            String encodedPassword = passwordEncoder.encode(ua.getPassword());
            ua.setPassword(encodedPassword);

            if(userRepo.findByUsername(ua.getUsername()).isPresent()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            publishMessageToPubSub(ua);
            userRepo.save(ua);

            Map<String, Object> userResponse = new HashMap<>();
            userResponse.put("username", ua.getUsername());
            userResponse.put("id", ua.getId());
            userResponse.put("firstname", ua.getFirstname());
            userResponse.put("lastname", ua.getLastname());
            userResponse.put("account_created", ua.getAccount_created());
            userResponse.put("account_updated", ua.getAccount_updated());
            return ResponseEntity.status(HttpStatus.CREATED).header("cache-control", "no-cache").body(userResponse);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
        }

    }

    private void publishMessageToPubSub(User user) {
        String projectId = "cloud-dev-415102";
        String topicId = "verify_email";
        Gson gson = new Gson();
        String jsonData = gson.toJson(user);

        ProjectTopicName topicName = ProjectTopicName.of(projectId, topicId);
        Publisher publisher = null;

        try {
            publisher = Publisher.newBuilder(topicName).build();
            ByteString data = ByteString.copyFromUtf8(jsonData);
            PubsubMessage pubsubMessage = PubsubMessage.newBuilder().setData(data).build();
            publisher.publish(pubsubMessage).get(); // Blocking publish
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (publisher != null) {
                publisher.shutdown(); // Always shutdown publisher
            }
        }
    }

    @GetMapping("/user/self")
    public ResponseEntity<?> getuser(HttpServletRequest request) {
        if(request.getContentLength()>0 || request.getQueryString()!=null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("cache-control", "no-cache").build();
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                Optional<User> userOptional = userRepo.findByUsername(userDetails.getUsername());

                Optional<Token> tok = tv.findById(userDetails.getUsername());
                if(tok.isEmpty()){
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                }
                if(!tok.get().isVerified()){
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).header("cache-control", "no-cache").body("Permission denied. User not verified !!");
                }

                try {
                     User ua = userOptional.get();
                    Map<String, Object> userResponse = new HashMap<>();
                    userResponse.put("username", ua.getUsername());
                    userResponse.put("id", ua.getId());
                    userResponse.put("firstname", ua.getFirstname());
                    userResponse.put("lastname", ua.getLastname());
                    userResponse.put("account_created", ua.getAccount_created());
                    userResponse.put("account_updated", ua.getAccount_updated());

                    return ResponseEntity.status(HttpStatus.OK).header("cache-control", "no-cache").body(userResponse);
                } catch (Exception e) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).header("cache-control", "no-cache").build();
                 }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).header("cache-control", "no-cache").build();

        }
    }

    @PutMapping("/user/self")
    public ResponseEntity<?> updateuser(HttpServletRequest request,@RequestBody User ur) {

        if(request.getQueryString()!=null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("cache-control", "no-cache").build();
        }

        if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Optional<User> userOptional = userRepo.findByUsername(userDetails.getUsername());

            Optional<Token> tok = tv.findById(userDetails.getUsername());
            if(tok.isEmpty()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            if(!tok.get().isVerified()){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).header("cache-control", "no-cache").body("Permission denied. User not verified !!");
            }

            try {
                if(ur.getUsername()!=null || ur.getId()!=null || ur.getAccount_created()!=null || ur.getAccount_updated()!=null){
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("cache-control", "no-cache").build();
                }

                if(ur.getPassword().isEmpty()){
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("cache-control", "no-cache").body("Password cannot be empty.");
                }
                User user = userOptional.get();
                user.setFirstname(ur.getFirstname());
                user.setLastname(ur.getLastname());
                user.setPassword(passwordEncoder.encode(ur.getPassword()));
                user.setAccount_updated(new Date());
                userRepo.save(user);



                Map<String, Object> userResponse = new HashMap<>();
                userResponse.put("username", user.getUsername());
                userResponse.put("id", user.getId());
                userResponse.put("firstname", user.getFirstname());
                userResponse.put("lastname", user.getLastname());
                userResponse.put("account_created", user.getAccount_created());
                userResponse.put("account_updated", user.getAccount_updated());
                return ResponseEntity.status(HttpStatus.NO_CONTENT).header("cache-control", "no-cache").body(userResponse);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("cache-control", "no-cache").build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).cacheControl(CacheControl.noCache()).build();

        }
    }

    @RequestMapping(value = "/user/self", method = {RequestMethod.DELETE, RequestMethod.PATCH, RequestMethod.POST})
    public ResponseEntity<?> handleuser(){
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).cacheControl(CacheControl.noCache()).build();
    }

    @RequestMapping(value = "/user", method = {RequestMethod.DELETE, RequestMethod.PATCH, RequestMethod.GET, RequestMethod.PUT})
    public ResponseEntity<?> handleeuser(){
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).cacheControl(CacheControl.noCache()).build();
    }
}
