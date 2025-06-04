package org.example.murilo.ordemservico.domain.payload;

import org.example.murilo.ordemservico.enumeration.OperationEnum;

public class CheckProfilePayload extends OperationPayload {

    private final String token;

    public CheckProfilePayload(final String token) {
        super(OperationEnum.LER_DADOS.getId());
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
