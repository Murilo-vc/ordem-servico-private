package org.example.murilo.ordemservico.handler.exception;

import org.example.murilo.ordemservico.domain.constants.ErrorMessages;
import org.example.murilo.ordemservico.enumeration.OperationEnum;

public class UsernameAlreadyExistsException extends BaseException {
    public UsernameAlreadyExistsException(final OperationEnum operation) {
        super(ErrorMessages.USERNAME_ALREADY_EXISTS, operation);
    }
}
