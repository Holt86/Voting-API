package ru.aovechnikov.voting.repository.converter;

import javax.persistence.AttributeConverter;

/**
 * Used to convert
 * entity attribute {@link Double} into database column representation {@link Integer}
 * and back again.
 * @author - A.Ovechnikov
 * @date - 11.01.2018
 */
public class PriceConverter implements AttributeConverter<Double, Integer> {

    /**
     * Converts the {@link Double} stored in the entity attribute into the
     * {@link Integer} representation to be stored in the database.
     *
     * @param attribute the entity attribute value {@link Double} to be converted
     * @return {@link Integer} the converted data to be stored in the database column
     */
    @Override
    public Integer convertToDatabaseColumn(Double attribute) {
        long priceToInt = Math.round(attribute * 100);
        return Math.toIntExact(priceToInt);
    }

    /**
     * Converts the {@link Integer} data stored in the database column into the
     * {@link Double} value to be stored in the entity attribute.
     *
     * @param dbData the data {@link Integer} from the database column to be converted into
     * @return {@link Double} the converted value to be stored in the entity attribute
     */
    @Override
    public Double convertToEntityAttribute(Integer dbData) {
        return dbData/100d;
    }
}
