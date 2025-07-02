package org.example.murilo.ordemservico.handler.exception;

public class UserNotFoundOnTableException extends Exception {
    public UserNotFoundOnTableException() {
        super("Usuario nao encontrado!");
    }

}
