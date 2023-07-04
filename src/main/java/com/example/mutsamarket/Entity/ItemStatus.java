package com.example.mutsamarket.Entity;

public enum ItemStatus {
    ON_SALE("판매중"), SOLD("판매 완료");
    final String status;

    ItemStatus(String status) {
        this.status = status;
    }
}
