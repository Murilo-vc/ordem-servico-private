package org.example.murilo.ordemservico.handler.exception;

import org.example.murilo.ordemservico.domain.constants.ErrorMessages;
import org.example.murilo.ordemservico.enumeration.OperationEnum;

public class NotWorkOrderOwner extends BaseException {

    public NotWorkOrderOwner(final OperationEnum operation) {
        super(ErrorMessages.NOT_WORK_ORDER_OWNER, operation);
    }
}
