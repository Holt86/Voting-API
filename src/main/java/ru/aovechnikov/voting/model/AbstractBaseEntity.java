package ru.aovechnikov.voting.model;

import ru.aovechnikov.voting.HasId;

/**
 * Simple entity with an id property. Used as a base class for entity.
 * needing this property.
 * @author - A.Ovechnikov
 * @date - 09.01.2018
 */

public abstract class AbstractBaseEntity implements HasId {

    private Integer id;

    public AbstractBaseEntity() {
    }

    public AbstractBaseEntity(Integer id) {
        this.id = id;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }



    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return String.format("Entity %s (%s)", getClass().getName(), getId());
    }
}
