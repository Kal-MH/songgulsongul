package com.smu.songgulsongul.data.post;

import com.google.gson.annotations.SerializedName;

public class Ccl {

    @SerializedName("ccl_cc") int cc;
    @SerializedName("ccl_a") int a;
    @SerializedName("ccl_nc") int nc;
    @SerializedName("ccl_nd") int nd;
    @SerializedName("ccl_sa") int sa;

    public int getCc() {
        return cc;
    }

    public int getA() {
        return a;
    }

    public int getNc() {
        return nc;
    }

    public int getNd() {
        return nd;
    }

    public int getSa() {
        return sa;
    }
}
