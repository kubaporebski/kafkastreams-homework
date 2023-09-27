package com.epam.bd201;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;

public class Expedia {

    public final static Schema AVRO_SCHEMA = new Schema.Parser().parse(
            "{\"type\":\"record\",\"name\":\"expedia\",\"namespace\":\"expedia\",\"fields\":" +
                    "[{\"name\":\"id\",\"type\":[\"null\",\"long\"],\"default\":null}," +
                    "{\"name\":\"date_time\",\"type\":[\"null\",\"string\"],\"default\":null}," +
                    "{\"name\":\"site_name\",\"type\":[\"null\",\"int\"],\"default\":null}," +
                    "{\"name\":\"posa_container\",\"type\":[\"null\",\"int\"],\"default\":null}," +
                    "{\"name\":\"user_location_country\",\"type\":[\"null\",\"int\"],\"default\":null}," +
                    "{\"name\":\"user_location_region\",\"type\":[\"null\",\"int\"],\"default\":null}," +
                    "{\"name\":\"user_location_city\",\"type\":[\"null\",\"int\"],\"default\":null}," +
                    "{\"name\":\"orig_destination_distance\",\"type\":[\"null\",\"double\"],\"default\":null}," +
                    "{\"name\":\"user_id\",\"type\":[\"null\",\"int\"],\"default\":null}," +
                    "{\"name\":\"is_mobile\",\"type\":[\"null\",\"int\"],\"default\":null}," +
                    "{\"name\":\"is_package\",\"type\":[\"null\",\"int\"],\"default\":null}," +
                    "{\"name\":\"channel\",\"type\":[\"null\",\"int\"],\"default\":null}," +
                    "{\"name\":\"srch_ci\",\"type\":[\"null\",\"string\"],\"default\":null}," +
                    "{\"name\":\"srch_co\",\"type\":[\"null\",\"string\"],\"default\":null}," +
                    "{\"name\":\"srch_adults_cnt\",\"type\":[\"null\",\"int\"],\"default\":null}," +
                    "{\"name\":\"srch_children_cnt\",\"type\":[\"null\",\"int\"],\"default\":null}," +
                    "{\"name\":\"srch_rm_cnt\",\"type\":[\"null\",\"int\"],\"default\":null}," +
                    "{\"name\":\"srch_destination_id\",\"type\":[\"null\",\"int\"],\"default\":null}," +
                    "{\"name\":\"srch_destination_type_id\",\"type\":[\"null\",\"int\"],\"default\":null}," +
                    "{\"name\":\"hotel_id\",\"type\":[\"null\",\"long\"],\"default\":null}]," +
            "\"connect.version\":1,\"connect.name\":\"expedia.expedia\"}");


    public static DatumReader<GenericRecord> getDatumReader() {
        return new GenericDatumReader<>(Expedia.AVRO_SCHEMA);
    }
}
