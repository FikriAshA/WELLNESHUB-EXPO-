package com.example.appexpo;

import java.time.LocalDate;

public class Kebiasaan {
    private String nama;
    private LocalDate tanggalMulai;
    private boolean selesai;

    public Kebiasaan(String nama, LocalDate tanggalMulai) {
        this.nama = nama;
        this.tanggalMulai = tanggalMulai;
        this.selesai = false;
    }

    public String getNama() {
        return nama;
    }

    public LocalDate getTanggalMulai() {
        return tanggalMulai;
    }

    public boolean isSelesai() {
        return selesai;
    }

    public void setSelesai(boolean selesai) {
        this.selesai = selesai;
    }
}
