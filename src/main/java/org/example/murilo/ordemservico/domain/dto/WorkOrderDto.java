package org.example.murilo.ordemservico.domain.dto;

import org.example.murilo.ordemservico.domain.entity.User;
import org.example.murilo.ordemservico.domain.entity.WorkOrder;

import java.util.List;
import java.util.Map;

public class WorkOrderDto {

    private Integer id;

    private String autor;

    private String descricao;

    private String status;

    protected WorkOrderDto(final Integer id,
                           final String autor,
                           final String descricao,
                           final String status) {
        this.id = id;
        this.autor = autor;
        this.descricao = descricao;
        this.status = status;
    }

    public static List<WorkOrderDto> toDto(final List<WorkOrder> orders,
                                           final Map<Integer, User> authorsMap) {
        return orders.stream()
            .map(wo -> WorkOrderDto.toDto(wo, authorsMap.getOrDefault(wo.getCreatedById(), null)))
            .toList();
    }

    public static WorkOrderDto toDto(final WorkOrder order,
                                     final User author) {
        if (author == null) {
            throw new IllegalArgumentException("Author cannot be null");
        }

        return new WorkOrderDto(
            order.getId(),
            author.getUsername(),
            order.getDescription(),
            order.getStatus().getId()
        );
    }

    public Integer getId() {
        return id;
    }

    public String getAutor() {
        return autor;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getStatus() {
        return status;
    }
}
