package org.example.murilo.ordemservico.domain.dto;

import org.example.murilo.ordemservico.domain.entity.User;

public class SmallUserDto {
    private String nome;
    private String usuario;
    private String perfil;

    public SmallUserDto(final String nome,
                        final String usuario,
                        final String perfil) {
        this.nome = nome;
        this.usuario = usuario;
        this.perfil = perfil;
    }

    public static SmallUserDto toDto(final User user) {
        return new SmallUserDto(
            user.getName(),
            user.getUsername(),
            user.getRole().getId()
        );
    }

    public String getNome() {
        return nome;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getPerfil() {
        return perfil;
    }
}
