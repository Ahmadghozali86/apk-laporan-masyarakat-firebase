package com.example.civilreportapps;

public class DataLaporan {

    public String nama;
    public String telepon;
    public String lokasi;
    public String tanggal;
    public  String isi;
    public String imglaporan;

    public String getJenisLaporan() {
        return jenisLaporan;
    }

    public void setJenisLaporan(String jenisLaporan) {
        this.jenisLaporan = jenisLaporan;
    }

    public String jenisLaporan;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String key;

    public DataLaporan(){

    }
    public DataLaporan( String nama, String telepon, String lokasi, String tanggal, String isi,String jenisLaporan, String imglaporan) {
        this.nama = nama;
        this.telepon = telepon;
        this.lokasi = lokasi;
        this.tanggal = tanggal;
        this.isi = isi;
        this.jenisLaporan=jenisLaporan;
        this.imglaporan = imglaporan;
    }

    public String getImglaporan() {
        return imglaporan;
    }

    public void setImglaporan(String imglaporan) {
        this.imglaporan = imglaporan;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getTelepon() {
        return telepon;
    }

    public void setTelepon(String telepon) {
        this.telepon = telepon;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getIsi() {
        return isi;
    }

    public void setIsi(String isi) {
        this.isi = isi;
    }
}
