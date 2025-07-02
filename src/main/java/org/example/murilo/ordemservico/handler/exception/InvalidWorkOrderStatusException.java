package org.example.murilo.ordemservico.handler.exception;

import org.example.murilo.ordemservico.domain.constants.ErrorMessages;
import org.example.murilo.ordemservico.enumeration.OperationEnum;

public class InvalidWorkOrderStatusException extends BaseException {

    public InvalidWorkOrderStatusException(final OperationEnum operation) {
        super(ErrorMessages.INVALID_WORK_ORDER_STATUS, operation);
    }
}
