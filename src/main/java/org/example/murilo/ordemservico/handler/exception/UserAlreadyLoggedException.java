package org.example.murilo.ordemservico.handler.exception;

import org.example.murilo.ordemservico.domain.constants.ErrorMessages;
import org.example.murilo.ordemservico.enumeration.OperationEnum;

public class UserAlreadyLoggedException extends BaseException {

    public UserAlreadyLoggedException() {
        super(ErrorMessages.USER_ALREADY_LOGGED, OperationEnum.LOGIN);
    }
}
