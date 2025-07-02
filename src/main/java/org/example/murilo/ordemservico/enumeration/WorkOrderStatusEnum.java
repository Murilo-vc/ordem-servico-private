package org.example.murilo.ordemservico.enumeration;

import java.util.List;

public enum WorkOrderStatusEnum {
    PENDENTE("pendente"),
    CANCELADA("cancelada"),
    FINALIZADA("finalizada");

    private final String id;

    WorkOrderStatusEnum(final String id) {
        this.id = id;
    }

    public static List<String> WORK_ORDER_STATUS_STRING() {
        return List.of(
            PENDENTE.id, CANCELADA.id, FINALIZADA.id
        );
    }

    public static WorkOrderStatusEnum getById(final String id) {
        return switch (id) {
            case "pendente" -> PENDENTE;
            case "cancelada" -> CANCELADA;
            case "finalizada" -> FINALIZADA;
            default -> throw new IllegalArgumentException("Id not supported");
        };
    }

    public String getId() {
        return id;
    }
}
