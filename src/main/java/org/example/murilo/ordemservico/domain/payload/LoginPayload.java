package org.example.murilo.ordemservico.domain.payload;

import org.example.murilo.ordemservico.enumeration.OperationEnum;

public class LoginPayload extends OperationPayload {

    private final String usuario;

    private final String senha;

    public LoginPayload(final String usuario,
                        final String senha) {
        super(OperationEnum.LOGIN.getId());
        this.usuario = usuario;
        this.senha = senha;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getSenha() {
        return senha;
    }
}
