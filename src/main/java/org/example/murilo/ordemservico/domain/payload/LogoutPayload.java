package org.example.murilo.ordemservico.domain.payload;

import org.example.murilo.ordemservico.enumeration.OperationEnum;

public class LogoutPayload extends OperationPayload {

    private final String token;

    public LogoutPayload(final String token) {
        super(OperationEnum.LOGOUT.getId());
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
