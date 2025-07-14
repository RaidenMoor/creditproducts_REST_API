package com.example.creditproducts.service.CreditApplication;

import com.example.creditproducts.exception.InvalidApplicationStatusException;
import com.example.creditproducts.model.ApplicationStatus;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@NoArgsConstructor
public class ApplicationStatusValidation {
    private static final Set<String> VALID_STATUSES = Set.of(
            "PENDING", "APPROVED", "REJECTED"
    );

    public ApplicationStatus validateStatus(String status) {
        if (!VALID_STATUSES.contains(status)) {
            throw new InvalidApplicationStatusException(status);
        }
        return ApplicationStatus.valueOf(status);
    }
}
