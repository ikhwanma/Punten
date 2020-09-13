package com.android.raionpunten;

public class User {
    private String nama,email,password,jabatan;

    public User() {
    }

    public User(String nama, String email, String password, String jabatan) {
        this.nama = nama;
        this.email = email;
        this.password = password;
        this.jabatan = jabatan;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getJabatan() {
        return jabatan;
    }

    public void setJabatan(String jabatan) {
        this.jabatan = jabatan;
    }
}
