package com.epam.bd201;

import expedia.ExpediaIn;
import expedia.ExpediaOut;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Single class for storing Expedia row AVRO format.
 */
public class Expedia {

    private final static Logger logger = LoggerFactory.getLogger(Expedia.class);

    public static final List<String> INPUT_FIELD_NAMES = ExpediaIn.getClassSchema()
            .getFields()
            .stream()
            .map(Schema.Field::name)
            .collect(Collectors.toList());

    public static DatumReader<GenericRecord> getDatumReader() {
        return new GenericDatumReader<>(ExpediaIn.getClassSchema());
    }

    /**
     * Processing an input row into output row.
     * Here we are calculating a duration of each check-in.
     *
     * @param row input expedia row
     * @return enriched expedia row
     */
    public static ExpediaOut process(ExpediaIn row) {

        // copy data from original row into the new one
        ExpediaOut newRow = new ExpediaOut();
        for (String field : INPUT_FIELD_NAMES) {
            newRow.put(field, row.get(field));
        }

        // perform a calculation of a duration of time spent in a given hotel
        String checkIn = Objects.toString(row.getSrchCi(), "");
        String checkOut = Objects.toString(row.getSrchCo(), "");
        String duration = calculateTextDuration(checkIn, checkOut).toString();
        newRow.setDuration(duration);

        return newRow;
    }

    enum DurationValue {
        ERR("Erroneous data"),
        SHORT("Short stay"),
        STANDARD("Standard stay"),
        STANDARD_EXT("Standard extended"),
        LONG("Long stay");

        private final String value;

        DurationValue(String value) {
            this.value = value;
        }

        public String toString() {
            return value;
        }
    }

    private static DurationValue calculateTextDuration(String strCheckIn, String strCheckOut) {
        try {
            if (strCheckIn.isEmpty() || strCheckOut.isEmpty())
                return DurationValue.ERR;

            LocalDate checkIn = LocalDate.parse(strCheckIn);
            LocalDate checkOut = LocalDate.parse(strCheckOut);
            Period duration = Period.between(checkIn, checkOut);

            long days = duration.getDays();
            if (days <= 0)
                return DurationValue.ERR;
            else if (days <= 4)
                return DurationValue.SHORT;
            else if (days <= 10)
                return DurationValue.STANDARD;
            else if (days <= 14)
                return DurationValue.STANDARD_EXT;
            else
                return DurationValue.LONG;

        } catch (Exception ex) {
            logger.info(String.format("Failed getting difference between %s and %s", strCheckIn, strCheckOut), ex);
            return DurationValue.ERR;
        }
    }
}
