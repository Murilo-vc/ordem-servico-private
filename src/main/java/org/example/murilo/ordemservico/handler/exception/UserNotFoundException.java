package org.example.murilo.ordemservico.handler.exception;

import org.example.murilo.ordemservico.domain.constants.ErrorMessages;
import org.example.murilo.ordemservico.enumeration.OperationEnum;

public class UserNotFoundException extends BaseException {

    public UserNotFoundException(final OperationEnum operation) {
        super(ErrorMessages.USER_NOT_FOUND, operation);
    }
}
