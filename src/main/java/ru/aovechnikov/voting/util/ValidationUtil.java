package ru.aovechnikov.voting.util;

import ru.aovechnikov.voting.HasId;
import ru.aovechnikov.voting.util.exception.NotFoundException;

/**
 * Class for data validation
 * @author - A.Ovechnikov
 * @date - 12.01.2018
 */
public class ValidationUtil {

    private ValidationUtil() {
    }

    public static <T> T checkNotFoundWithId(T entity, int id) {
        checkNotFound(entity, "id=" + id);
        return entity;
    }

    public static <T> T checkNotFound(T entity, String msg){
        checkNotFound(entity != null, msg);
        return entity;
    }

    public static void checkNotFound(boolean found, int id){
        checkNotFound(found, "id=" + id);
    }

    public static void checkNotFound(boolean found, String msg){
        if (!found){
            throw new NotFoundException( msg);
        }
    }
    /**
     * Checks that {@link HasId} is new, otherwise throws exception {@link IllegalArgumentException }.
     */
    public static void checkNew(HasId bean) {
        if (!bean.isNew()) {
            throw new IllegalArgumentException(bean + " must be new (id=null)");
        }
    }

    /**
     * Checks that {@link HasId#getId()} equals id  otherwise {@link IllegalArgumentException }.
     * if {@link HasId#getId()} is null,  then it is assigned the passed value id.
     */
    public static void checkIdConsistent(HasId bean, int id){
     if (bean.isNew()){
         bean.setId(id);
     }else if (bean.getId() != id){
         throw new IllegalArgumentException(String.format("%s must be with id=%d", bean, id));
     }
    }

    /**
     * Finds the root cause of thrown {@link Throwable}.
     * @param t thrown {@link Throwable}
     * @return root {@link Throwable}
     */
    public static Throwable getRootCause(Throwable t) {
        Throwable result = t;
        Throwable cause;

        while (null != (cause = result.getCause()) && (result != cause)) {
            result = cause;
        }
        return result;
    }
}
