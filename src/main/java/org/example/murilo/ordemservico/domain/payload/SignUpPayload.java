package org.example.murilo.ordemservico.domain.payload;

import org.example.murilo.ordemservico.enumeration.OperationEnum;

public class SignUpPayload extends OperationPayload {

    private String nome;
    private String usuario;
    private String senha;
    private String perfil;
    private String token;

    public SignUpPayload(final String nome,
                         final String usuario,
                         final String senha,
                         final String perfil,
                         final String token) {
        super(OperationEnum.CADASTRO.getId());
        this.nome = nome;
        this.usuario = usuario;
        this.senha = senha;
        this.perfil = perfil;
        this.token = token;
    }

    public String getNome() {
        return nome;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getSenha() {
        return senha;
    }

    public String getPerfil() {
        return perfil;
    }

    public String getToken() {
        return token;
    }
}
