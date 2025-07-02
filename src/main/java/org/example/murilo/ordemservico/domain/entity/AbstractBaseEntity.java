package org.example.murilo.ordemservico.domain.entity;

public class AbstractBaseEntity {

    protected Integer id;

    public AbstractBaseEntity(final Integer id) {
        this.id = id;
    }

    public AbstractBaseEntity() {
        this.id = null;
    }

    public Integer getId() {
        return id;
    }
}
