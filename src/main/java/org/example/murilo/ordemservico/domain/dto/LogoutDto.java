package org.example.murilo.ordemservico.domain.dto;

import org.example.murilo.ordemservico.domain.constants.SuccessMessages;
import org.example.murilo.ordemservico.enumeration.OperationEnum;
import org.example.murilo.ordemservico.enumeration.ResponseStatusEnum;

public class LogoutDto extends BaseResponseDto {

    protected LogoutDto(final String status,
                        final String operacao,
                        final String mensagem) {
        super(status, operacao, mensagem);
    }

    public static LogoutDto toDto() {
        return new LogoutDto(
            ResponseStatusEnum.SUCESSO.getId(),
            OperationEnum.LOGOUT.getId(),
            SuccessMessages.LOGOUT_SUCCESSFUL
        );
    }
}
