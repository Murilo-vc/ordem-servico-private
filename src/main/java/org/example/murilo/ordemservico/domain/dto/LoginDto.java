package org.example.murilo.ordemservico.domain.dto;

import org.example.murilo.ordemservico.enumeration.OperationEnum;
import org.example.murilo.ordemservico.enumeration.ResponseStatusEnum;
import org.example.murilo.ordemservico.enumeration.UserRoleEnum;

public class LoginDto extends BaseResponseDto {

    private final String token;

    private final String perfil;

    protected LoginDto(final String status,
                       final String operacao,
                       final String mensagem,
                       final String token,
                       final String perfil) {
        super(status, operacao, mensagem);
        this.token = token;
        this.perfil = perfil;
    }

    public static LoginDto toDto(final String token,
                                 final UserRoleEnum role) {
        return new LoginDto(
            ResponseStatusEnum.SUCESSO.getId(),
            OperationEnum.LOGIN.getId(),
            null,
            token,
            role.getId()
        );
    }

    public String getToken() {
        return token;
    }

    public String getPerfil() {
        return perfil;
    }
}
