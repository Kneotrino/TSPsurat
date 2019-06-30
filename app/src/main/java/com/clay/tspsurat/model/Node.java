package com.clay.tspsurat.model;

import com.orm.SugarRecord;

public class Node extends SugarRecord {

    private String  Nama,Keterangan;
    private int     kelas;

    @Override
    public String toString() {
        return "{" +
                "ID='" + getId() + '\'' +
                "-'" + Nama + '\'' +
                "-'" + Keterangan + '\'' +
//                ", kelas=" + kelas +
//                ", Nomor=" + Nomor +
//                ", lat=" + lat +
//                ", lng=" + lng +
                '}';
    }

    public Node() {
    }

    public Node(String nama, int kelas, int nomor, Double lat, Double lng) {
        Nama = nama;
        this.kelas = kelas;
        Nomor = nomor;
        this.lat = lat;
        this.lng = lng;
    }

    public Node(String nama, String keterangan, int kelas, int nomor, Double lat, Double lng) {
        Nama = nama;
        Keterangan = keterangan;
        this.kelas = kelas;
        Nomor = nomor;
        this.lat = lat;
        this.lng = lng;
    }

    public String getKeterangan() {
        return Keterangan;
    }

    public void setKeterangan(String keterangan) {
        Keterangan = keterangan;
    }

    public int getNomor() {
        return Nomor;
    }

    public void setNomor(int nomor) {
        Nomor = nomor;
    }

    private int Nomor;
    private Double  lat;
    private Double  lng;

    public String getNama() {
        return Nama;
    }

    public void setNama(String nama) {
        Nama = nama;
    }

    public int getKelas() {
        return kelas;
    }

    public void setKelas(int kelas) {
        this.kelas = kelas;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }
}
