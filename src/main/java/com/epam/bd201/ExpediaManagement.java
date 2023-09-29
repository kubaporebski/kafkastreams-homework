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
 * Single class for managing Expedia-related things.
 */
public final class ExpediaManagement {

    /** Not to be instantiated */
    private ExpediaManagement() { }

    private final static Logger logger = LoggerFactory.getLogger(ExpediaManagement.class);

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
     * Here we are calculating a duration of each hotel stay, and the result is stored in the output row.
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

        // perform calculation of duration of time spent in a given hotel
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

    /**
     * Returning the right category of the duration between two given dates.
     * Categories returned are as follows:
     * - "Erroneous data": null, less than or equal to zero,
     * - "Short stay": 1-4 days,
     * - "Standard stay": 5-10 days,
     * - "Standard extended stay": 11-14 days,
     * - "Long stay": 2 weeks plus.
     *
     * @param strCheckIn check-in (start) date, non-null string, may be empty
     * @param strCheckOut check-out (end) date, non-null string, may be empty
     * @return one of the DurationValue instance
     */
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
