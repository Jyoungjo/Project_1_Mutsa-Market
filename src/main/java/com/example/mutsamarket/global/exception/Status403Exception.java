package com.example.mutsamarket.global.exception;

public abstract class Status403Exception extends RuntimeException{
    public Status403Exception(String message) {
        super(message);
    }
}
