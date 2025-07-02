package org.example.murilo.ordemservico.enumeration;

public enum WorkOrderFilterEnum {
    PENDENTE("pendente"),
    CANCELADA("cancelada"),
    FINALIZADA("finalizada"),
    TODAS("todas");

    private final String id;

    WorkOrderFilterEnum(final String id) {
        this.id = id;
    }

    public static WorkOrderFilterEnum getById(final String id) {
        return switch (id) {
            case "pendente" -> PENDENTE;
            case "cancelada" -> CANCELADA;
            case "finalizada" -> FINALIZADA;
            case "todas" -> TODAS;
            default -> throw new IllegalArgumentException("Id not supported");
        };
    }

    public String getId() {
        return id;
    }
}
