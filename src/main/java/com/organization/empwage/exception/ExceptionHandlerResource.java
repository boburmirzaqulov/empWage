package com.organization.empwage.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.organization.empwage.dto.ValidatorDto;
import com.organization.empwage.helper.AppMessages;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ExceptionHandlerResource{
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ValidatorDto>> handleException(MethodArgumentNotValidException e) {
        List<ValidatorDto> list = new ArrayList<>();
        e.getBindingResult().getAllErrors()
                .forEach(er -> {
                    String fieldName = ((FieldError) er).getField();
                    String message = er.getDefaultMessage();
                    list.add(new ValidatorDto(fieldName, message));
                });
        return ResponseEntity.status(400).body(list);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ValidatorDto> handleDatabaseException(ValidationException e){
        return ResponseEntity.status(400).body(new ValidatorDto(e.getMessage(), AppMessages.VALIDATOR_MESSAGE));
    }

    @ExceptionHandler(ModelException.class)
    public ResponseEntity<ValidatorDto> handleDatabaseException(ModelException e){
        return ResponseEntity.status(400).body(new ValidatorDto(e.getMessage(), AppMessages.FILE_SAVE_ERROR));
    }
}