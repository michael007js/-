package com.blankj.utilcode.model;

/**
 * Created by leilei on 2017/12/26.
 */

import android.graphics.Bitmap;


public class ContactVo {

    private long id;
    private String name;
    private String moble;
    private Bitmap img;

    public ContactVo() {
        super();
    }

    public ContactVo(long id, String name, String moble, Bitmap img) {
        super();
        this.id = id;
        this.name = name;
        this.moble = moble;
        this.img = img;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMoble() {
        return moble;
    }

    public void setMoble(String moble) {
        this.moble = moble;
    }

    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }

    @Override
    public String toString() {
        return "ContactVo [id=" + id + ", name=" + name + ", moble=" + moble
                + ", img=" + img + "]";
    }


}
