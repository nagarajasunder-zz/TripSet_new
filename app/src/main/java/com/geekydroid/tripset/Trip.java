package com.geekydroid.tripset;

public class Trip {
    private String t_id,t_name,t_desc,t_amt,t_date,grp_count;

    public Trip(String t_id, String t_name, String t_desc, String t_amt, String t_date, String grp_count) {
        this.t_id = t_id;
        this.t_name = t_name;
        this.t_desc = t_desc;
        this.t_amt = t_amt;
        this.t_date = t_date;
        this.grp_count = grp_count;
    }

    public String getT_id() {
        return t_id;
    }

    public void setT_id(String t_id) {
        this.t_id = t_id;
    }

    public String getT_name() {
        return t_name;
    }

    public void setT_name(String t_name) {
        this.t_name = t_name;
    }

    public String getT_desc() {
        return t_desc;
    }

    public void setT_desc(String t_desc) {
        this.t_desc = t_desc;
    }

    public String getT_amt() {
        return t_amt;
    }

    public void setT_amt(String t_amt) {
        this.t_amt = t_amt;
    }

    public String getT_date() {
        return t_date;
    }

    public void setT_date(String t_date) {
        this.t_date = t_date;
    }

    public String getGrp_count() {
        return grp_count;
    }

    public void setGrp_count(String grp_count) {
        this.grp_count = grp_count;
    }
}
