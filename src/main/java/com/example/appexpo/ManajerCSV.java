package com.example.appexpo;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ManajerCSV {
    public static class Tugas {
        private String tanggal;
        private String nama;
        private boolean selesai;

        public Tugas(String tanggal, String nama, boolean selesai) {
            this.tanggal = tanggal;
            this.nama = nama;
            this.selesai = selesai;
        }

        public String getTanggal() { return tanggal; }
        public String getNama() { return nama; }
        public boolean isSelesai() { return selesai; }
        public void setSelesai(boolean selesai) { this.selesai = selesai; }

        @Override
        public String toString() {
            return String.format("%s - %s (%s)",
                    tanggal, nama, selesai ? "Selesai" : "Belum");
        }
    }

    private final Path path = Paths.get("data/data_tugas.csv");

    public List<Tugas> bacaSemuaTugas() {
        List<Tugas> list = new ArrayList<>();
        if (!Files.exists(path)) {
            return list;
        }

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String baris;
            reader.readLine();
            while ((baris = reader.readLine()) != null) {
                String[] bagian = baris.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                if (bagian.length < 3) continue;

                String tanggal = bagian[0].replace("\"", "");
                String nama = bagian[1].replace("\"", "");
                boolean selesai = bagian[2].trim().equals("1");
                list.add(new Tugas(tanggal, nama, selesai));
            }
        } catch (IOException e) {
            System.err.println("Error membaca file: " + e.getMessage());
        }
        return list;
    }

    public void simpanSemuaTugas(List<Tugas> tugasList) {
        try {
            Files.createDirectories(path.getParent());

            try (BufferedWriter writer = Files.newBufferedWriter(path,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {

                writer.write("tanggal,nama_tugas,selesai\n");
                for (Tugas t : tugasList) {
                    writer.write(String.format("\"%s\",\"%s\",%d\n",
                            t.getTanggal(), t.getNama(), t.isSelesai() ? 1 : 0));
                }
            }
        } catch (IOException e) {
            System.err.println("Error menyimpan file: " + e.getMessage());
        }
    }

    public Tugas buatTugasBaru(String nama) {
        return new Tugas(LocalDate.now().toString(), nama, false);
    }

    public void updateStatusTugas(Tugas tugas, boolean selesai) {
        tugas.setSelesai(selesai);
    }
}