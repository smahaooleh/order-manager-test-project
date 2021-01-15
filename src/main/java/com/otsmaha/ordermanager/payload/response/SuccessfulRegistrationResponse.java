package com.otsmaha.ordermanager.payload.response;

import java.time.LocalDateTime;

public class SuccessfulRegistrationResponse {

    private String message;

    private Long id;

    private LocalDateTime creationDate;

    private LocalDateTime validUntil;

    public SuccessfulRegistrationResponse(String message, Long id, LocalDateTime creationDate, LocalDateTime validUntil) {
        this.message = message;
        this.id = id;
        this.creationDate = creationDate;
        this.validUntil = validUntil;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(LocalDateTime validUntil) {
        this.validUntil = validUntil;
    }
}
