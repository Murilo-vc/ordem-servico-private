package org.example.murilo.ordemservico.enumeration;

import java.util.List;

public enum OperationEnum {
    LOGIN("login"),
    LOGOUT("logout"),
    LER_DADOS("ler_dados"),
    CADASTRO("cadastro"),
    EDITAR_USUARIO("editar_usuario"),
    EXCLUIR_USUARIO("excluir_usuario"),
    LISTAR_USUARIOS("listar_usuarios"),
    CADASTRAR_ORDEM("cadastrar_ordem"),
    LISTAR_ORDENS("listar_ordens"),
    EDITAR_ORDEM("editar_ordem"),
    ALTERAR_ORDEM("alterar_ordem");

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
        return List.of(
            CADASTRAR_ORDEM.id, LISTAR_ORDENS.id, EDITAR_ORDEM.id, ALTERAR_ORDEM.id
        );
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
            case "cadastrar_ordem" -> CADASTRAR_ORDEM;
            case "listar_ordens" -> LISTAR_ORDENS;
            case "editar_ordem" -> EDITAR_ORDEM;
            case "alterar_ordem" -> ALTERAR_ORDEM;
            default -> null;
        };
    }

    public String getId() {
        return id;
    }
}
