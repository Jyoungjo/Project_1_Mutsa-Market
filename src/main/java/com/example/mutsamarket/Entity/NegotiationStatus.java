package com.example.mutsamarket.Entity;

public enum NegotiationStatus {
    PROPOSED("제안"), ACCEPTED("수락"), REJECTED("거절"), CONFIRMED("확정");
    private final String status;

    NegotiationStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
