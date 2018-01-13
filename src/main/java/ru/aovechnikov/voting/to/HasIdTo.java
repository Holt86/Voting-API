package ru.aovechnikov.voting.to;

import ru.aovechnikov.voting.HasId;

/**
 * Base class for DTO with {@code id}
 *
 * @author - A.Ovechnikov
 * @date - 12.01.2018
 */

abstract public class HasIdTo implements HasId {

    protected Integer id;

    public HasIdTo() {
    }

    public HasIdTo(Integer id) {
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
}
