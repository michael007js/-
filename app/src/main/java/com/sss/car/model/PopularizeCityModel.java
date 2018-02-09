package com.sss.car.model;

import java.util.List;

/**
 * Created by leilei on 2018/1/19.
 */

public class PopularizeCityModel {


    /**
     * status : 1
     * data : [{"id":1,"name":"北京","letter":"beijing","acronym":"bj","pid":0,"level":1,"lng":"116.409444","lat":"39.905236"},{"id":2,"name":"天津","letter":"tianjin","acronym":"tj","pid":0,"level":1,"lng":"117.197921","lat":"39.09062"},{"id":9,"name":"上海","letter":"shanghai","acronym":"sh","pid":0,"level":1,"lng":"121.457207","lat":"31.231405"}]
     * message :
     */


    private String id;
    private String name;
    private String letter;
    private String acronym;
    private int pid;
    private int level;
    private String lng;
    private String lat;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }
}
