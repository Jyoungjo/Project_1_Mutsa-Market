package com.example.mutsamarket.exceptions.status401;

public abstract class Status401Exception extends RuntimeException{
    public Status401Exception(String message) {
        super(message);
    }
}
