package com.fabiancsaba.csapa;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "filmek")
public class Film {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cim")
    private String cim;

    @Column(name = "megjelenes_datuma")
    private LocalDate megjelenesDatuma;

    @Column(name = "kategoria")
    private String kategoria;

    @Column(name = "bent_van")
    private boolean bentVan;

    @Column(name = "mennyiseg")
    private int mennyiseg;

    @Column(name = "film_kep")
    private byte[] filmKep;


    public Film(String cim, LocalDate megjelenesDatuma, String kategoria, boolean bentVan, int mennyiseg) {
        this.cim = cim;
        this.megjelenesDatuma = megjelenesDatuma;
        this.kategoria = kategoria;
        this.bentVan = bentVan;
        this.mennyiseg = mennyiseg;
    }

    public Film() {

    }


    public Long getId() {
        return id;
    }

    public String getCim() {
        return cim;
    }

    public LocalDate getMegjelenesDatuma() {
        return megjelenesDatuma;
    }

    public String getKategoria() {
        return kategoria;
    }

    public boolean isBentVan() {
        return bentVan;
    }

    public void setBentVan(boolean bentVan) {
        this.bentVan = bentVan;
    }

    public int getMennyiseg() {
        return mennyiseg;
    }

    public void setMennyiseg(int mennyiseg) {
        this.mennyiseg = mennyiseg;
    }

    public byte[] getFilmKep() {
        return filmKep;
    }

    public void setFilmKep(byte[] filmKep) {
        this.filmKep = filmKep;
    }

    @Override
    public String toString() {
        return "Film :" +
                "cím= '" + cim + '\'' +
                ", megjelenés dátuma= " + megjelenesDatuma +
                ", kategória= '" + kategoria + '\'' +
                ", bent van= " + bentVan +
                ", mennyiség= " + mennyiseg +
                ", kép= " + (filmKep != null ? "van" : "nincs") + // Ellenőrzi, hogy van-e kép
                '}';
    }
}