package org.example.murilo.ordemservico.domain.dto;

import org.example.murilo.ordemservico.domain.constants.SuccessMessages;
import org.example.murilo.ordemservico.enumeration.OperationEnum;
import org.example.murilo.ordemservico.enumeration.ResponseStatusEnum;

public class CreateUserDto extends BaseResponseDto {

    public CreateUserDto(final String status,
                         final String operacao,
                         final String mensagem) {
        super(status, operacao, mensagem);
    }

    public static CreateUserDto toDto() {
        return new CreateUserDto(
            ResponseStatusEnum.SUCESSO.getId(),
            OperationEnum.CADASTRO.getId(),
            SuccessMessages.USER_CREATED_SUCCESSFULLY
        );
    }
}
