package com.example.baitap04.model;

public class MonHoc {
    private String name;
    private String desc;
    private int pic;

    public MonHoc(String name, String desc, int pic) {
        this.name = name;
        this.desc = desc;
        this.pic = pic;
    }

    public int getPic() {
        return pic;
    }

    public void setPic(int pic) {
        this.pic = pic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
