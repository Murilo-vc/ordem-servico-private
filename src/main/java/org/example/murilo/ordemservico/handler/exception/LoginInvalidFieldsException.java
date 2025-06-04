package org.example.murilo.ordemservico.handler.exception;

import org.example.murilo.ordemservico.domain.constants.ErrorMessages;
import org.example.murilo.ordemservico.enumeration.OperationEnum;

public class LoginInvalidFieldsException extends BaseException {

    public LoginInvalidFieldsException() {
        super(ErrorMessages.LOGIN_INVALID_FIELDS, OperationEnum.LOGIN);
    }
}
