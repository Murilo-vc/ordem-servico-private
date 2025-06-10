package org.example.murilo.ordemservico.domain.payload;

import org.example.murilo.ordemservico.enumeration.OperationEnum;

public class DeleteUserPayload extends OperationPayload {

    private final String token;

    private String usuario_alvo;

    public DeleteUserPayload(final String token) {
        super(OperationEnum.EXCLUIR_USUARIO.getId());
        this.token = token;
        this.usuario_alvo = null;
    }

    public DeleteUserPayload(final String token,
                             final String usuario_alvo) {
        super(OperationEnum.EXCLUIR_USUARIO.getId());
        this.token = token;
        this.usuario_alvo = usuario_alvo;
    }

    public String getToken() {
        return token;
    }

    public String getUsuario_alvo() {
        return usuario_alvo;
    }
}
