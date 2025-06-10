package org.example.murilo.ordemservico.domain.dto;

import org.example.murilo.ordemservico.enumeration.OperationEnum;

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

    public static BaseResponseDto toDto(final String status,
                                        final OperationEnum operation,
                                        final String message) {
        return new BaseResponseDto(status, operation.getId(), message);
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
