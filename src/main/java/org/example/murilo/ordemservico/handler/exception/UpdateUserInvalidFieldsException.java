package org.example.murilo.ordemservico.handler.exception;

import org.example.murilo.ordemservico.domain.constants.ErrorMessages;
import org.example.murilo.ordemservico.enumeration.OperationEnum;

public class UpdateUserInvalidFieldsException extends BaseException {

    public UpdateUserInvalidFieldsException() {
        super(ErrorMessages.UPDATE_USER_INVALID_FIELDS, OperationEnum.EDITAR_USUARIO);
    }
}
