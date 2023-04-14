package com.maaz.wallet.controller;

import com.maaz.wallet.dto.ValidationErrorResponse;
import com.maaz.wallet.dto.Violation;
import com.maaz.wallet.dto.WalletIdempotencyErrorResponse;
import com.maaz.wallet.dto.WalletValidationErrorResponse;
import com.maaz.wallet.exception.WalletIdempotencyException;
import com.maaz.wallet.exception.WalletValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

@ControllerAdvice
class ErrorHandlingControllerAdvice {

    @ExceptionHandler(WalletIdempotencyException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    WalletIdempotencyErrorResponse onWalletIdempotencyException(WalletIdempotencyException e) {
        return new WalletIdempotencyErrorResponse(e.getMessage());
    }

    @ExceptionHandler(WalletValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    WalletValidationErrorResponse onWalletValidationException(WalletValidationException e) {
        return new WalletValidationErrorResponse(e.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ValidationErrorResponse onConstraintValidationException(ConstraintViolationException e) {
        ValidationErrorResponse error = new ValidationErrorResponse();
        for (ConstraintViolation violation : e.getConstraintViolations()) {
            error.getViolations().add(new Violation(violation.getPropertyPath().toString(), violation.getMessage()));
        }
        return error;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ValidationErrorResponse onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ValidationErrorResponse error = new ValidationErrorResponse();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            error.getViolations().add(new Violation(fieldError.getField(), fieldError.getDefaultMessage()));
        }
        return error;
    }

}

