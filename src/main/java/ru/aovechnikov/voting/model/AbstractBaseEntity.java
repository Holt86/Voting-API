package ru.aovechnikov.voting.model;

import org.hibernate.Hibernate;
import ru.aovechnikov.voting.HasId;

import javax.persistence.*;

/**
 * Simple entity with an id property. Used as a base class for entity.
 * needing this property.
 * @author - A.Ovechnikov
 * @date - 09.01.2018
 */
@MappedSuperclass
@Access(AccessType.FIELD)
public abstract class AbstractBaseEntity implements HasId {

    public static final int START_SEQ = 100;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "global_seq")
    @SequenceGenerator(name = "global_seq", sequenceName = "global_seq", allocationSize = 1, initialValue = START_SEQ)
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
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != Hibernate.getClass(obj)) return false;

        AbstractBaseEntity that = (AbstractBaseEntity) obj;

        return getId() != null && getId().equals(that.getId());
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
