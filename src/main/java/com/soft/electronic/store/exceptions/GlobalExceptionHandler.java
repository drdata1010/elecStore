package com.soft.electronic.store.exceptions;

import com.soft.electronic.store.dtos.ApiResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @ExceptionHandler(ResourceNotFound.class) //have named ResourceNotFound instead of ResourceNotFoundException
    public ResponseEntity<ApiResponseMessage> resourceNotFoundExceptionHandler(ResourceNotFound ex){
        logger.info("Exception handler invoked !!");
        ApiResponseMessage responseMessage = ApiResponseMessage.builder().message(ex.getMessage()).status(HttpStatus.NOT_FOUND).success(true).build();

        return new ResponseEntity<>(responseMessage,HttpStatus.NOT_FOUND);
    }

    //MethodArgumentNotValidException --coming in ValidAPI
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){

        List<ObjectError> allErrors = ex.getBindingResult().getAllErrors(); //taking out all errors
        Map<String,Object> response = new HashMap<>();

        allErrors.stream().forEach(objectError -> {
            String field = ((FieldError) objectError).getField(); //this is string key
            String message = objectError.getDefaultMessage(); //this is error message
            response.put(field,message);
        });

        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }
    //handle bad api request
    @ExceptionHandler(BadApiRequestException.class) //have named ResourceNotFound instead of ResourceNotFoundException
    public ResponseEntity<ApiResponseMessage> handleBadApiRequest(BadApiRequestException ex){
        logger.info("Bad Api Request !!");
        ApiResponseMessage responseMessage = ApiResponseMessage.builder().message(ex.getMessage()).status(HttpStatus.BAD_REQUEST).success(false).build();
        return new ResponseEntity<>(responseMessage,HttpStatus.BAD_REQUEST);
    }
}
