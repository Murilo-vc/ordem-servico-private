package org.example.murilo.ordemservico.handler.exception;

import org.example.murilo.ordemservico.enumeration.OperationEnum;

public class BaseException extends Exception {

    private final OperationEnum operation;

    public BaseException(final String message,
                         final OperationEnum operation) {
        super(message);
        this.operation = operation;
    }

    public OperationEnum getOperation() {
        return operation;
    }
}
