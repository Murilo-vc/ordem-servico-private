package org.example.murilo.ordemservico.handler.exception;

import org.example.murilo.ordemservico.domain.constants.ErrorMessages;
import org.example.murilo.ordemservico.enumeration.OperationEnum;

public class NoUsersFoundException extends BaseException {
    public NoUsersFoundException() {
        super(ErrorMessages.NO_USERS_FOUND, OperationEnum.LISTAR_USUARIOS);
    }
}
