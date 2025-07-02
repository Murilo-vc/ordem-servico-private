package org.example.murilo.ordemservico.handler.exception;

import org.example.murilo.ordemservico.domain.constants.ErrorMessages;
import org.example.murilo.ordemservico.enumeration.OperationEnum;

public class InvalidDescriptionException extends BaseException {

    public InvalidDescriptionException(final OperationEnum operation) {
        super(ErrorMessages.INVALID_DESCRIPTION, operation);
    }
}
