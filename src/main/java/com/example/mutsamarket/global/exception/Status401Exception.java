package com.example.mutsamarket.global.exception;

public abstract class Status401Exception extends RuntimeException{
    public Status401Exception(String message) {
        super(message);
    }
}
