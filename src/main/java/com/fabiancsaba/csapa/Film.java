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

    // Konstruktorok, getterek és setterek

    public Film() {}

    public Film(String cim, LocalDate megjelenesDatuma, String kategoria, boolean bentVan) {
        this.cim = cim;
        this.megjelenesDatuma = megjelenesDatuma;
        this.kategoria = kategoria;
        this.bentVan = bentVan;
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

    // toString() metódus felüldefiniálása
    @Override
    public String toString() {
        return "Film :" +
                "cím= '" + cim + '\'' +
                ", megjelenés dátuma= " + megjelenesDatuma +
                ", kategória= '" + kategoria + '\'' +
                ", bent van= " + bentVan +
                '}';
    }
}