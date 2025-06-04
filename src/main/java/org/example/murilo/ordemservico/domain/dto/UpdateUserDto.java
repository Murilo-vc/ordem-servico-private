package org.example.murilo.ordemservico.domain.dto;

import org.example.murilo.ordemservico.domain.constants.SuccessMessages;
import org.example.murilo.ordemservico.enumeration.OperationEnum;
import org.example.murilo.ordemservico.enumeration.ResponseStatusEnum;

public class UpdateUserDto extends BaseResponseDto {

    private final String token;

    public UpdateUserDto(final String status,
                         final String operacao,
                         final String mensagem,
                         final String token) {
        super(status, operacao, mensagem);
        this.token = token;
    }

    public static UpdateUserDto toDto(final String token) {
        return new UpdateUserDto(
            ResponseStatusEnum.SUCESSO.getId(),
            OperationEnum.EDITAR_USUARIO.getId(),
            SuccessMessages.UPDATE_OWN_USER_SUCCESSFUL,
            token
        );
    }

    public String getToken() {
        return token;
    }
}
