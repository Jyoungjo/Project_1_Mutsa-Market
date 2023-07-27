package com.example.mutsamarket.exceptions.status404;

public abstract class Status404Exception extends RuntimeException{
    public Status404Exception(String message) {
        super(message);
    }
}
