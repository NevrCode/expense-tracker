package com.nevrcode.expense_tracker.service;


import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;

@Service
public class ValidationService {
    @Autowired
    private Validator validator;

    public void validate(Object req) {
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(req);

        if (constraintViolations.size() != 0) {
            throw new ConstraintViolationException(constraintViolations);
        }

    }

}
