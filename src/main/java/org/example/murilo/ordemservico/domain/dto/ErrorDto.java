package org.example.murilo.ordemservico.domain.dto;

import org.example.murilo.ordemservico.enumeration.ResponseStatusEnum;
import org.example.murilo.ordemservico.handler.exception.BaseException;

public class ErrorDto extends BaseResponseDto{

    public ErrorDto(final String status,
                       final String operacao,
                       final String mensagem) {
        super(status, operacao, mensagem);
    }

    public static ErrorDto toDto(final BaseException e) {
        return new ErrorDto(
            ResponseStatusEnum.ERRO.getId(),
            e.getOperation().getId(),
            e.getMessage()
        );
    }
}
