package ru.aovechnikov.voting.web.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;


/**
 * Class that to serialize {@link Double}
 * into JSON in in a specific decimal format "#0.00",
 * using provided {@link JsonGenerator}
 *
 * @author - A.Ovechnikov
 * @date - 11.01.2018
 */
public class DoubleToPriceViewSerializer extends JsonSerializer<Double> {

    public static final DecimalFormat PRICE_FORMAT = new DecimalFormat("#0.00");

    static {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        PRICE_FORMAT.setDecimalFormatSymbols(symbols);
    }

    /**
     * Method is invoked to serialize a double in a specific decimal format "#0.00".
     *
     * @param aDouble Value to serialize;
     * @param jsonGenerator Generator used to output resulting Json content;
     * @param serializerProvider Provider that can be used to get serializers.
     * @throws IOException
     */
    @Override
    public void serialize(Double aDouble, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(PRICE_FORMAT.format(aDouble));
    }
}
