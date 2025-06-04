package org.example.murilo.ordemservico.enumeration;

import java.util.List;

public enum OperationEnum {
    LOGIN("login"),
    LOGOUT("logout"),
    LER_DADOS("ler_dados"),
    CADASTRO("cadastro"),
    EDITAR_USUARIO("editar_usuario"),
    EXCLUIR_USUARIO("excluir_usuario");

    private final String id;

    OperationEnum(final String id) {
        this.id = id;
    }

    public static List<String> ALL_USERS_OPERATIONS_STRING() {
        return List.of(
            LOGIN.id, LOGOUT.id, LER_DADOS.id, CADASTRO.id, EDITAR_USUARIO.id, EXCLUIR_USUARIO.id
        );
    }

    public static List<String> ADM_USERS_OPERATIONS_STRING() {
        return List.of();
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
            default -> null;
        };
    }

    public String getId() {
        return id;
    }
}
