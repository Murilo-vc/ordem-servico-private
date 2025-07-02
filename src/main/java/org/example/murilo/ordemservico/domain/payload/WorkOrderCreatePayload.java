package org.example.murilo.ordemservico.domain.payload;

import org.example.murilo.ordemservico.enumeration.OperationEnum;

public class WorkOrderCreatePayload extends OperationPayload {

    private String descricao;

    private String token;

    public WorkOrderCreatePayload(final String descricao,
                                  final String token) {
        super(OperationEnum.CADASTRAR_ORDEM.getId());
        this.descricao = descricao;
        this.token = token;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getToken() {
        return token;
    }
}
