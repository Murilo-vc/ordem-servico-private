package org.example.murilo.ordemservico.handler.exception;

import org.example.murilo.ordemservico.domain.constants.ErrorMessages;
import org.example.murilo.ordemservico.enumeration.OperationEnum;

public class SignUpInvalidFieldsException extends BaseException {

    public SignUpInvalidFieldsException() {
        super(ErrorMessages.SIGN_UP_INVALID_FIELDS, OperationEnum.CADASTRO);
    }
}
