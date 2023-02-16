package com.matsa.clients;

public class ConflictException extends RuntimeException{
    ConflictException(String message){
        super(message);
    }
}
