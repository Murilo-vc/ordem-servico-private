package org.example.murilo.ordemservico.domain.entity;

import org.example.murilo.ordemservico.enumeration.WorkOrderStatusEnum;

public class WorkOrder extends AbstractBaseEntity {

    private String description;

    private WorkOrderStatusEnum status;

    private Integer createdById;

    public WorkOrder(final Integer id,
                     final String description,
                     final WorkOrderStatusEnum status,
                     final Integer createdById) {
        super(id);
        this.description = description;
        this.status = status;
        this.createdById = createdById;
    }

    public WorkOrder(final String description,
                     final WorkOrderStatusEnum status,
                     final Integer createdById) {
        this.description = description;
        this.status = status;
        this.createdById = createdById;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public WorkOrderStatusEnum getStatus() {
        return status;
    }

    public void setStatus(WorkOrderStatusEnum status) {
        this.status = status;
    }

    public Integer getCreatedById() {
        return createdById;
    }
}
