package com.fabiancsaba.csapa;


import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "kintlevoseg")
public class kintlevoseg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nev")
    private String nev;

    @Column(name = "telefonszam")
    private String telefonszam;

    @Column(name = "kivetel_datuma")
    private LocalDate kivetelDatuma;

    public kintlevoseg(Long id, String nev, String telefonszam, LocalDate kivetelDatuma) {
        this.id = id;
        this.nev = nev;
        this.telefonszam = telefonszam;
        this.kivetelDatuma = kivetelDatuma;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNev() {
        return nev;
    }

    public void setNev(String nev) {
        this.nev = nev;
    }

    public String getTelefonszam() {
        return telefonszam;
    }

    public void setTelefonszam(String telefonszam) {
        this.telefonszam = telefonszam;
    }

    public LocalDate getKivetelDatuma() {
        return kivetelDatuma;
    }

    public void setKivetelDatuma(LocalDate kivetelDatuma) {
        this.kivetelDatuma = kivetelDatuma;
    }
}
