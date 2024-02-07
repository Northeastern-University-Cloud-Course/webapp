package com.project.application.controller;

import org.springframework.http.CacheControl;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import com.project.application.User;
import org.eclipse.jdt.core.compiler.InvalidInputException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.datasource.lookup.DataSourceLookupFailureException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.NoHandlerFoundException;
import javax.naming.ServiceUnavailableException;

@ControllerAdvice
public class GlobalErrorHandler {
        @ExceptionHandler(InvalidInputException.class)
        public ResponseEntity<User> InvalidInputException(InvalidInputException ie){
            System.err.println(ie.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<User>MethodArgumentNotValidException(MethodArgumentNotValidException me){
            System.err.println(me.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        @ExceptionHandler(NullPointerException.class)
        public ResponseEntity<User>NullPointerException(NullPointerException np){
            System.err.println(np.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        @ExceptionHandler(HttpMessageNotReadableException.class)
        public ResponseEntity<User>HttpMessageNotRedable(){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        @ExceptionHandler(NoHandlerFoundException.class)
        public ResponseEntity<User>NoHandlerFoundException(NoHandlerFoundException nh){
            System.err.println(nh.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        @ExceptionHandler(ServiceUnavailableException.class)
        public ResponseEntity<?>DataAccessException(ServiceUnavailableException dae){
            System.err.println(dae.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(null);
        }
        @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
        public ResponseEntity<?>UnsupportedMediaType(){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<User> handleAccessDeniedExceptionHandler(AccessDeniedException ae){
        System.err.println(ae.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).cacheControl(CacheControl.noCache()).body(null);
    }
}
