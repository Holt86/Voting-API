package ru.aovechnikov.voting;


import org.springframework.hateoas.Identifiable;

/**
 *  Simple interface for objects with an id property.
 * @author - A.Ovechnikov
 * @date - 09.01.2018
 */

public interface HasId extends Identifiable<Integer> {

    /**
     * Returns the id of the object.
     *
     * @return id
     */
    Integer getId();

    /**
     * Set id for object
     * @param id of the object
     */
    void setId(Integer id);

    /**
     * @return true if id is null
     */
    default boolean isNew(){
        return getId() == null;
    }
}
