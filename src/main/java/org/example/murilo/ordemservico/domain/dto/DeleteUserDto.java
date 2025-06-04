package org.example.murilo.ordemservico.domain.dto;

import org.example.murilo.ordemservico.domain.constants.SuccessMessages;
import org.example.murilo.ordemservico.enumeration.OperationEnum;
import org.example.murilo.ordemservico.enumeration.ResponseStatusEnum;

public class DeleteUserDto extends BaseResponseDto {

    public DeleteUserDto(String status, String operacao, String mensagem) {
        super(status, operacao, mensagem);
    }

    public static DeleteUserDto toDto() {
        return new DeleteUserDto(
            ResponseStatusEnum.SUCESSO.getId(),
            OperationEnum.EXCLUIR_USUARIO.getId(),
            SuccessMessages.DELETE_OWN_USER_SUCCESSFUL
        );
    }
}
