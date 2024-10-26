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

    @Column(name = "visszahozatal")
    private LocalDate visszahozatal;

    @Column(name = "filmid")
    private long filmId;

    @Column(name = "filmcim")
    private String filmcim;

    public kintlevoseg(Long id, String nev, String telefonszam, LocalDate kivetelDatuma, LocalDate visszahozatal, long filmId, String filmcim) {
        this.id = id;
        this.nev = nev;
        this.telefonszam = telefonszam;
        this.kivetelDatuma = kivetelDatuma;
        this.visszahozatal = LocalDate.now();
        this.filmId = filmId;
        this.filmcim = filmcim;
    }

    public kintlevoseg() {

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

    public LocalDate getVisszahozatal() {
        return visszahozatal;
    }

    public void setVisszahozatal(LocalDate visszahozatal) {
        this.visszahozatal = visszahozatal;
    }

    public long getFilmId() {
        return filmId;
    }

    public void setFilmId(long filmId) {
        this.filmId = filmId;
    }

    public String getFilmcim() {
        return filmcim;
    }

    public void setFilmcim(String filmcim) {
        this.filmcim = filmcim;
    }
}
