package org.example.murilo.ordemservico.domain.payload;

import org.example.murilo.ordemservico.enumeration.OperationEnum;

public class WorkOrderUpdatePayload extends OperationPayload {

    private Integer id_ordem;

    private String nova_descricao;

    private String token;

    public WorkOrderUpdatePayload(final Integer id_ordem,
                                  final String nova_descricao,
                                  final String token) {
        super(OperationEnum.EDITAR_ORDEM.getId());
        this.id_ordem = id_ordem;
        this.nova_descricao = nova_descricao;
        this.token = token;
    }

    public Integer getId_ordem() {
        return id_ordem;
    }

    public String getNova_descricao() {
        return nova_descricao;
    }

    public String getToken() {
        return token;
    }
}
