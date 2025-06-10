package org.example.murilo.ordemservico.enumeration;

import java.util.List;

public enum OperationEnum {
    LOGIN("login"),
    LOGOUT("logout"),
    LER_DADOS("ler_dados"),
    CADASTRO("cadastro"),
    EDITAR_USUARIO("editar_usuario"),
    EXCLUIR_USUARIO("excluir_usuario"),
    LISTAR_USUARIOS("listar_usuarios");

    private final String id;

    OperationEnum(final String id) {
        this.id = id;
    }

    public static List<String> ALL_USERS_OPERATIONS_STRING() {
        return List.of(
            LOGIN.id, LOGOUT.id, LER_DADOS.id, CADASTRO.id, EDITAR_USUARIO.id, EXCLUIR_USUARIO.id, LISTAR_USUARIOS.id
        );
    }

    public static List<String> WORK_ORDER_OPERATIONS_STRING() {
        return List.of();
    }

    public static OperationEnum getById(final String id) {
        return switch (id) {
            case "login" -> LOGIN;
            case "logout" -> LOGOUT;
            case "ler_dados" -> LER_DADOS;
            case "cadastro" -> CADASTRO;
            case "editar_usuario" -> EDITAR_USUARIO;
            case "excluir_usuario" -> EXCLUIR_USUARIO;
            case "listar_usuarios" -> LISTAR_USUARIOS;
            default -> null;
        };
    }

    public String getId() {
        return id;
    }
}
