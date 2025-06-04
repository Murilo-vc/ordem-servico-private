package org.example.murilo.ordemservico.enumeration;

public enum ResponseStatusEnum {
    SUCESSO("sucesso"),
    ERRO("erro");

    private final String id;

    ResponseStatusEnum(final String id) {
        this.id = id;
    }

    public static ResponseStatusEnum getById(final String id) {
        return switch (id) {
            case "sucesso" -> SUCESSO;
            case "erro" -> ERRO;
            default -> null;
        };
    }

    public String getId() {
        return id;
    }
}
