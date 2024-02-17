package com.convertPdf.officepdfconverter.advice;

import com.convertPdf.officepdfconverter.exception.DocumentConversionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DocumentConversionException.class)
    public ResponseEntity<String> handleDocumentConversionException(DocumentConversionException e) {
        // Aquí puedes personalizar el mensaje o la estructura de la respuesta según sea necesario
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Aquí puedes añadir más manejadores para diferentes tipos de excepciones si es necesario
}
