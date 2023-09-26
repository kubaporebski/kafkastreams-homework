package com.epam.bd201;

import org.apache.avro.Schema;

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

    private long id;
    private String date_time;
    private String site_name;
    private int posa_container;
    private int user_location_country;
    private int user_location_region;
    private int user_location_city;
    private double orig_destination_distance;
    private int user_id;
    private int is_mobile;
    private int is_package;
    private int channel;
    private String srch_ci;
    private String srch_o;
    private int srch_adults_cnt;
    private int srch_children_cnt;
    private int srch_rm_cnt;
    private int srch_destination_id;
    private int srch_destination_type_id;
    private long hotel_id;

    public Expedia() { }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getSite_name() {
        return site_name;
    }

    public void setSite_name(String site_name) {
        this.site_name = site_name;
    }

    public int getPosa_container() {
        return posa_container;
    }

    public void setPosa_container(int posa_container) {
        this.posa_container = posa_container;
    }

    public int getUser_location_country() {
        return user_location_country;
    }

    public void setUser_location_country(int user_location_country) {
        this.user_location_country = user_location_country;
    }

    public int getUser_location_region() {
        return user_location_region;
    }

    public void setUser_location_region(int user_location_region) {
        this.user_location_region = user_location_region;
    }

    public int getUser_location_city() {
        return user_location_city;
    }

    public void setUser_location_city(int user_location_city) {
        this.user_location_city = user_location_city;
    }

    public double getOrig_destination_distance() {
        return orig_destination_distance;
    }

    public void setOrig_destination_distance(double orig_destination_distance) {
        this.orig_destination_distance = orig_destination_distance;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getIs_mobile() {
        return is_mobile;
    }

    public void setIs_mobile(int is_mobile) {
        this.is_mobile = is_mobile;
    }

    public int getIs_package() {
        return is_package;
    }

    public void setIs_package(int is_package) {
        this.is_package = is_package;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public String getSrch_ci() {
        return srch_ci;
    }

    public void setSrch_ci(String srch_ci) {
        this.srch_ci = srch_ci;
    }

    public String getSrch_o() {
        return srch_o;
    }

    public void setSrch_o(String srch_o) {
        this.srch_o = srch_o;
    }

    public int getSrch_adults_cnt() {
        return srch_adults_cnt;
    }

    public void setSrch_adults_cnt(int srch_adults_cnt) {
        this.srch_adults_cnt = srch_adults_cnt;
    }

    public int getSrch_children_cnt() {
        return srch_children_cnt;
    }

    public void setSrch_children_cnt(int srch_children_cnt) {
        this.srch_children_cnt = srch_children_cnt;
    }

    public int getSrch_rm_cnt() {
        return srch_rm_cnt;
    }

    public void setSrch_rm_cnt(int srch_rm_cnt) {
        this.srch_rm_cnt = srch_rm_cnt;
    }

    public int getSrch_destination_id() {
        return srch_destination_id;
    }

    public void setSrch_destination_id(int srch_destination_id) {
        this.srch_destination_id = srch_destination_id;
    }

    public int getSrch_destination_type_id() {
        return srch_destination_type_id;
    }

    public void setSrch_destination_type_id(int srch_destination_type_id) {
        this.srch_destination_type_id = srch_destination_type_id;
    }

    public long getHotel_id() {
        return hotel_id;
    }

    public void setHotel_id(long hotel_id) {
        this.hotel_id = hotel_id;
    }
}
