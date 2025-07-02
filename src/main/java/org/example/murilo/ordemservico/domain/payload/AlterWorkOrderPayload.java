package org.example.murilo.ordemservico.domain.payload;

import org.example.murilo.ordemservico.enumeration.OperationEnum;

public class AlterWorkOrderPayload extends OperationPayload {

    private final Integer id_ordem;

    private final String novo_status;

    private final String nova_descricao;

    private final String token;

    public AlterWorkOrderPayload(final Integer id_ordem,
                                 final String novo_status,
                                 final String nova_descricao,
                                 final String token) {
        super(OperationEnum.ALTERAR_ORDEM.getId());
        this.id_ordem = id_ordem;
        this.novo_status = novo_status;
        this.nova_descricao = nova_descricao;
        this.token = token;
    }

    public Integer getId_ordem() {
        return id_ordem;
    }

    public String getNovo_status() {
        return novo_status;
    }

    public String getNova_descricao() {
        return nova_descricao;
    }

    public String getToken() {
        return token;
    }
}
