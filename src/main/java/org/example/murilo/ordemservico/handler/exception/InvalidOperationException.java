package org.example.murilo.ordemservico.handler.exception;

import org.example.murilo.ordemservico.domain.constants.ErrorMessages;

public class InvalidOperationException extends BaseException {

    public InvalidOperationException() {
        super(ErrorMessages.INVALID_OPERATION, null);
    }
}
