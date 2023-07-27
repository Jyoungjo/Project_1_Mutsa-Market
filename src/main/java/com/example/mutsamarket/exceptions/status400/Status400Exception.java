package com.example.mutsamarket.exceptions.status400;

public abstract class Status400Exception extends RuntimeException{
    public Status400Exception(String message) {
        super(message);
    }
}
