package org.example.murilo.ordemservico.handler.exception;

import org.example.murilo.ordemservico.domain.constants.ErrorMessages;
import org.example.murilo.ordemservico.enumeration.OperationEnum;

public class WorkOrderClosedOrCanceledException extends BaseException {

    public WorkOrderClosedOrCanceledException(final OperationEnum operation) {
        super(ErrorMessages.WORK_ORDER_CLOSED_OR_CANCELED, operation);
    }
}
