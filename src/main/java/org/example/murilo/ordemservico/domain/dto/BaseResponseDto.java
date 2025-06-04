package org.example.murilo.ordemservico.domain.dto;

public class BaseResponseDto {

    private final String status;

    private final String operacao;

    private final String mensagem;

    protected BaseResponseDto(final String status,
                              final String operacao,
                              final String mensagem) {
        this.status = status;
        this.operacao = operacao;
        this.mensagem = mensagem;
    }

    public String getStatus() {
        return status;
    }

    public String getOperacao() {
        return operacao;
    }

    public String getMensagem() {
        return mensagem;
    }
}
