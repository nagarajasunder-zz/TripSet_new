package com.geekydroid.tripset;

public class Expense_by_person {
    private String id,name,amt,category,time,desc,share_by;

    public Expense_by_person(String id, String spent_by, String amt, String category, String time, String desc, String share_by) {
        this.id = id;
        this.name = spent_by;
        this.amt = amt;
        this.category = category;
        this.time = time;
        this.desc = desc;
        this.share_by = share_by;
    }

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

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getShare_by() {
        return share_by;
    }

    public void setShare_by(String share_by) {
        this.share_by = share_by;
    }
}
