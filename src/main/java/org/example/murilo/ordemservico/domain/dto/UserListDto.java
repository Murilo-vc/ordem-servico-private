package org.example.murilo.ordemservico.domain.dto;

import org.example.murilo.ordemservico.enumeration.OperationEnum;
import org.example.murilo.ordemservico.enumeration.ResponseStatusEnum;

import java.util.List;

public class UserListDto extends BaseResponseDto {
    private List<SmallUserDto> usuarios;

    public UserListDto(final String status,
                       final String operacao,
                       final String mensagem,
                       List<SmallUserDto> usuarios) {
        super(status, operacao, mensagem);
        this.usuarios = usuarios;
    }

    public static UserListDto toDto(final List<SmallUserDto> users) {
        return new UserListDto(
            ResponseStatusEnum.SUCESSO.getId(),
            OperationEnum.LISTAR_USUARIOS.getId(),
            null,
            users
        );
    }

    public List<SmallUserDto> getUsuarios() {
        return usuarios;
    }
}
