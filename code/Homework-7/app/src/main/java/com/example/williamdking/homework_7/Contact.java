package com.example.williamdking.homework_7;

/**
 * Created by William.D.King on 2015/12/4.
 */
public class Contact {
    private String _no;
    private String _name;
    private String _pnumber;

    public Contact(String no, String name, String pnumber) {
        this._no = no;
        this._name = name;
        this._pnumber = pnumber;
    }

    public String get_no() {
        return this._no;
    }
    public String get_name() {
        return this._name;
    }
    public String get_pnumber() {
        return this._pnumber;
    }
}
