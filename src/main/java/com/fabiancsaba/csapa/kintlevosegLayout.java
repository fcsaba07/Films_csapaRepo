package com.fabiancsaba.csapa;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.html.H1;
import java.util.List;

public class kintlevosegLayout extends VerticalLayout {

    private Grid<kintlevoseg> kintlevosegEntityGrid;
    private kintlevosegService kintlevosegService;
    private final filmService filmService;

    public kintlevosegLayout(kintlevosegService kintlevosegService, filmService filmService) {
        this.kintlevosegService = kintlevosegService;
        this.filmService = filmService;
        add(new H1("Kintlevőségek"));
        setupKintlevosegGrid();
        add(kintlevosegEntityGrid);
    }

    private void setupKintlevosegGrid() {
        kintlevosegEntityGrid = new Grid<>(kintlevoseg.class);
        kintlevosegEntityGrid.setItems(getkintlevoseg());
        kintlevosegEntityGrid.setColumns("filmcim", "nev", "telefonszam", "kivetelDatuma", "visszahozatal");
        kintlevosegEntityGrid.addComponentColumn(this::createReturnButton).setHeader("Visszahoz");
    }

    private Button createReturnButton(kintlevoseg kintlevoseg) {
        Button returnButton = new Button("Visszahoz");
        returnButton.addClickListener(event -> returnFilm(kintlevoseg));
        return returnButton;
    }

    private void returnFilm(kintlevoseg kintlevoseg) {
        long filmID = kintlevoseg.getFilmId();
        Film film = filmService.getFIlmById(filmID);
        if (film.getMennyiseg()==0){
            film.setBentVan(true);
        }
        film.setMennyiseg(film.getMennyiseg()+1);

        filmService.SaveFilm(film);
        kintlevosegService.deleteKintlevoseg(kintlevoseg);

        kintlevosegEntityGrid.setItems(getkintlevoseg());
    }

    public void refreshKintlevoseg(){
        kintlevosegEntityGrid.setItems(getkintlevoseg());
    }

    private List<kintlevoseg> getkintlevoseg() {
        return kintlevosegService.getAllKintlevoseg();
    }
}