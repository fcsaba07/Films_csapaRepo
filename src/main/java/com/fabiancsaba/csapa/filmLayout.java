package com.fabiancsaba.csapa;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.textfield.TextField;


import java.time.LocalDate;
import java.util.List;



public class filmLayout extends VerticalLayout {

    private final filmService filmService;
    private final kintlevosegService kintlevosegService;
    private Grid<Film> filmGrid;

    public filmLayout(filmService filmService, kintlevosegService kintlevosegService) {
        this.filmService = filmService;
        this.kintlevosegService = kintlevosegService;

        add(new H1("Filmek Listája"));
        setupFilmGrid();
        add(filmGrid);
    }

    private void setupFilmGrid() {
        filmGrid = new Grid<>(Film.class);
        filmGrid.setItems(filmService.getAllFilms());
        filmGrid.setColumns("cim", "kategoria", "megjelenesDatuma", "mennyiseg");
        filmGrid.addComponentColumn(this::createRentButton).setHeader("");
        filmGrid.addComponentColumn(this::createRemoveButton).setHeader("");
    }

    private Button createRentButton(Film film) {
        Button rentButton = new Button("Kivétel");
        rentButton.addClickListener(e -> rentFilmFunction(film));
        if (film.isBentVan()) {
            return rentButton;
        }
        return null;
    }

    private Button createRemoveButton(Film film) {
        Button removeButton = new Button("Törlés");
        removeButton.addClickListener(e -> removeFilmFuncion(film));
        return removeButton;
    }

    private void rentFilmFunction(Film film) {
        Dialog dialog = new Dialog();
        dialog.setCloseOnEsc(true);
        dialog.setCloseOnOutsideClick(true);

        TextField nameTextfield = new TextField("Bérlő Neve");
        TextField phoneTextfield = new TextField("Telefonszám");
        DatePicker returnDateField = new DatePicker("Visszahozatal dátuma");

        LocalDate currentDate = LocalDate.now();
        returnDateField.setValue(currentDate);

        Button saveButton = new Button("Kivétel");

        saveButton.addClickListener(e -> {

            // kintlevoseg példányosítása
            kintlevoseg newKintlevoseg = new kintlevoseg();
            newKintlevoseg.setFilmcim(film.getCim());
            newKintlevoseg.setFilmId(film.getId());
            newKintlevoseg.setNev(nameTextfield.getValue());
            newKintlevoseg.setTelefonszam(phoneTextfield.getValue());
            newKintlevoseg.setVisszahozatal(returnDateField.getValue());
            newKintlevoseg.setKivetelDatuma(currentDate);

            kintlevosegService.saveKintlevoseg(newKintlevoseg);


            film.setMennyiseg(film.getMennyiseg() - 1);
            if (film.getMennyiseg() == 0) {
                film.setBentVan(false);
            }
            filmService.SaveFilm(film);
            filmGrid.setItems(getFilms());

            dialog.close();
        });
        dialog.add(nameTextfield, phoneTextfield, returnDateField, saveButton);
        dialog.open();
    }

    private void removeFilmFuncion(Film film) {
        List<Long> kintlevosegFilmIds = kintlevosegService.getAllFilmIdsInKintlevoseg();

        if (kintlevosegFilmIds.contains(film.getId())) {
            Notification.show("Hiba: A filmet kivették. Nem törölhető amíg nem kerül vissza", 3000, Notification.Position.MIDDLE);
        } else {
            filmService.removeFilm(film.getId());
            filmGrid.setItems(getFilms());
        }
    }

    public void refreshfilms() {
        filmGrid.setItems(getFilms());
    }

    private List<Film> getFilms() {
        return filmService.getAllFilms();
    }
}
