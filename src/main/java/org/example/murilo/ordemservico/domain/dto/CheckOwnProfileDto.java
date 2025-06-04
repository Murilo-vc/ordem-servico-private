package org.example.murilo.ordemservico.domain.dto;

import org.example.murilo.ordemservico.domain.entity.User;
import org.example.murilo.ordemservico.enumeration.OperationEnum;
import org.example.murilo.ordemservico.enumeration.ResponseStatusEnum;

public class CheckOwnProfileDto extends BaseResponseDto {

    private final ProfileDto dados;

    protected CheckOwnProfileDto(final String status,
                                 final String operacao,
                                 final String mensagem,
                                 final ProfileDto dados) {
        super(status, operacao, mensagem);
        this.dados = dados;
    }

    public static CheckOwnProfileDto toDto(final User user) {
        return new CheckOwnProfileDto(
            ResponseStatusEnum.SUCESSO.getId(),
            OperationEnum.LER_DADOS.getId(),
            null,
            ProfileDto.toDto(user)
        );
    }

    public ProfileDto getDados() {
        return dados;
    }
}
