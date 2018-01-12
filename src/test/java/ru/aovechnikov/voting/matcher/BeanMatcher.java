package ru.aovechnikov.voting.matcher;


import org.junit.Assert;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Used to compare objects  or collections of those objects
 * using the passed implementation {@link BeanMatcher.Equality#areEqual(Object, Object)}
 *
 * @author - A.Ovechnikov
 * @date - 12.01.2018
 */

public class BeanMatcher<T> {

    public interface Equality<T> {
        boolean areEqual(T expected, T actual);
    }

    private Equality<T> equality;

    public BeanMatcher() {
        this((T expected, T actual) -> expected == actual || String.valueOf(expected).equals(String.valueOf(actual)));
    }

    public BeanMatcher(Equality<T> equality) {
        this.equality = equality;
    }

    private class Wrapper {

        private T entity;

        public Wrapper(T entity) {
            this.entity = entity;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Wrapper that = (Wrapper) o;
            return this.entity != null ? equality.areEqual(this.entity, that.entity) : that.entity == null;
        }

        @Override
        public String toString() {
            return String.valueOf(entity);
        }
    }

    public Wrapper wrap(T entity) {
        return new Wrapper(entity);
    }

    public Collection<Wrapper> wrapCollection(Collection<T> entities) {
        return entities.stream().map(this::wrap).collect(Collectors.toList());
    }

    public void assertEquals(T expect, T actual) {
        Assert.assertEquals(wrap(expect), wrap(actual));
    }

    public void assertCollectionsEquals(Collection<T> expected, Collection<T> actual) {
        Assert.assertEquals(wrapCollection(expected), wrapCollection(actual));
    }
}