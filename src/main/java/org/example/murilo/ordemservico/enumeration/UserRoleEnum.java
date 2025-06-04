package org.example.murilo.ordemservico.enumeration;

public enum UserRoleEnum {
    COMUM("comum"),
    ADM("adm");

    private final String id;

    UserRoleEnum(final String id) {
        this.id = id;
    }

    public static UserRoleEnum getById(final String id) {
        return switch (id) {
            case "comum" -> COMUM;
            case "adm" -> ADM;
            default -> null;
        };
    }

    public String getId() {
        return id;
    }
}
