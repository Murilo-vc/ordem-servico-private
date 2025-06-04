package org.example.murilo.ordemservico.domain.payload;

import org.example.murilo.ordemservico.enumeration.OperationEnum;

public class UpdateUserPayload extends OperationPayload {

    private final String token;

    private final String novo_usuario;

    private final String novo_nome;

    private final String nova_senha;

    public UpdateUserPayload(final String token,
                             final String novo_usuario,
                             final String novo_nome,
                             final String nova_senha) {
        super(OperationEnum.EDITAR_USUARIO.getId());
        this.token = token;
        this.novo_usuario = novo_usuario;
        this.novo_nome = novo_nome;
        this.nova_senha = nova_senha;
    }

    public String getToken() {
        return token;
    }

    public String getNovo_usuario() {
        return novo_usuario;
    }

    public String getNovo_nome() {
        return novo_nome;
    }

    public String getNova_senha() {
        return nova_senha;
    }
}
