package com.maaz.wallet.dto;

import java.util.ArrayList;
import java.util.List;

public class ValidationErrorResponse {

    private final List<Violation> violations = new ArrayList<>();

    private final String message = "Some fields are not valid";

    public String getMessage() {
        return message;
    }

    public List<Violation> getViolations() {
        return violations;
    }
}

