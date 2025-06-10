package org.example.murilo.ordemservico.handler.exception;

public class UserNotFoundException extends Exception {
    public UserNotFoundException() {
        super("Usuario nao encontrado!");
    }

}
