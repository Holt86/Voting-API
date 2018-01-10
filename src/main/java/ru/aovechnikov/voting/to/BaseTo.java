package ru.aovechnikov.voting.to;

import ru.aovechnikov.voting.HasId;

abstract public class BaseTo implements HasId {

    protected Integer id;

    public BaseTo() {
    }

    public BaseTo(Integer id) {
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
