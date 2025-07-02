package org.example.murilo.ordemservico.handler.exception;

import org.example.murilo.ordemservico.domain.constants.ErrorMessages;
import org.example.murilo.ordemservico.enumeration.OperationEnum;

public class WorkOrderNotFoundException extends BaseException {

    public WorkOrderNotFoundException(OperationEnum operation) {
        super(ErrorMessages.WORK_ORDER_NOT_FOUND, operation);
    }
}
