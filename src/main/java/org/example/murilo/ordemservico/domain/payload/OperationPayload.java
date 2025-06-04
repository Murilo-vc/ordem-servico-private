package org.example.murilo.ordemservico.domain.payload;

public class OperationPayload {

    private final String operacao;

    public OperationPayload(final String operacao) {
        this.operacao = operacao;
    }

    public String getOperacao() {
        return operacao;
    }
}
