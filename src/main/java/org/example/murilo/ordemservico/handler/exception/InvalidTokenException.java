package org.example.murilo.ordemservico.handler.exception;

import org.example.murilo.ordemservico.domain.constants.ErrorMessages;
import org.example.murilo.ordemservico.enumeration.OperationEnum;

public class InvalidTokenException extends BaseException {

    public InvalidTokenException(final OperationEnum operation) {
        super(ErrorMessages.INVALID_TOKEN, operation);
    }
}
