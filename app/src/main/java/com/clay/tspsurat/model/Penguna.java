package com.clay.tspsurat.model;

import com.orm.SugarRecord;

public class Penguna extends SugarRecord {
    String nama,username,userpass,nip;
    int level;


    public Penguna() {
    }

    public Penguna(String nama, String username, String userpass, String nip, int level) {
        this.nama = nama;
        this.username = username;
        this.userpass = userpass;
        this.nip = nip;
        this.level = level;
    }

    @Override
    public String toString() {
        return "Penguna {" +
                "nama='" + nama + '\'' +
                ", nip='" + nip + '\'' +
                ", ID='" + getId() + '\'' +
                '}'
                ;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserpass() {
        return userpass;
    }

    public void setUserpass(String userpass) {
        this.userpass = userpass;
    }

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
