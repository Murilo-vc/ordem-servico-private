package org.example.murilo.ordemservico.domain.payload;

import org.example.murilo.ordemservico.enumeration.OperationEnum;

public class UserListPayload extends OperationPayload {

    private final String token;

    public UserListPayload(final String token) {
        super(OperationEnum.LISTAR_USUARIOS.getId());
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
