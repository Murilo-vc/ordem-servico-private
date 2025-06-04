package org.example.murilo.ordemservico.domain.payload;

import org.example.murilo.ordemservico.enumeration.OperationEnum;

public class DeleteUserPayload extends OperationPayload {

    private final String token;

    public DeleteUserPayload(final String token) {
        super(OperationEnum.EXCLUIR_USUARIO.getId());
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
