package org.example.murilo.ordemservico.domain.payload;

import org.example.murilo.ordemservico.enumeration.OperationEnum;

public class WorkOrderListPayload extends OperationPayload {

    private String filtro;

    private String token;

    public WorkOrderListPayload(final String filtro,
                                final String token) {
        super(OperationEnum.LISTAR_ORDENS.getId());
        this.filtro = filtro;
        this.token = token;
    }

    public String getFiltro() {
        return filtro;
    }

    public String getToken() {
        return token;
    }
}
