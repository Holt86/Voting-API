package ru.aovechnikov.voting.model;

/**
 * Simple entity adds a name property to {@link AbstractBaseEntity}. Used as a base class for objects
 * needing these properties.
 *
 * @author - A.Ovechnikov
 * @date - 09.01.2018
 */

public abstract class AbstractNamedEntity extends AbstractBaseEntity {

    protected String name;

    public AbstractNamedEntity() {
    }

    public AbstractNamedEntity(Integer id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("Entity %s (%s, '%s')", getClass().getName(), getId(), name);
    }
}
