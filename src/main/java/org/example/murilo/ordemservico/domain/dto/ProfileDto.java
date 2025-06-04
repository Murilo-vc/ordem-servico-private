package org.example.murilo.ordemservico.domain.dto;

import org.example.murilo.ordemservico.domain.entity.User;

public class ProfileDto {

    private final String nome;

    private final String usuario;

    private final String senha;

    public ProfileDto(final String nome,
                      final String usuario,
                      final String senha) {
        this.nome = nome;
        this.usuario = usuario;
        this.senha = senha;
    }

    public static ProfileDto toDto(final User user) {
        return new ProfileDto(
            user.getName(),
            user.getUsername(),
            user.getPassword()
        );
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
}
