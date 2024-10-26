package com.fabiancsaba.csapa;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

@Route("")
public class csapaAppView extends VerticalLayout {

    private final filmService filmService;
    private final com.fabiancsaba.csapa.kintlevosegService kintlevosegService;
    private Grid<Film> filmGrid;
    private Grid<kintlevoseg> kintlevosegEntityGrid;

    @Autowired
    public csapaAppView(filmService filmService, kintlevosegService kintlevosegService) {
        this.filmService = filmService;
        this.kintlevosegService = kintlevosegService;

        // Tabok létrehozása
        Tab filmTab = new Tab("Filmek");
        Tab kintlevosegTab = new Tab("Kintlevoseg");

        // Filmek layout
        VerticalLayout filmLayout = createFilmLayout();

        // Kintlevoseg layout
        VerticalLayout kintlevosegLayout = createKintlevosegLayout();

        // Tabs kezelés
        Tabs tabs = new Tabs(filmTab, kintlevosegTab);
        tabs.addSelectedChangeListener(event -> {
            if (event.getSelectedTab().equals(filmTab)) {
                filmLayout.setVisible(true);
                kintlevosegLayout.setVisible(false);
            } else {
                filmLayout.setVisible(false);
                kintlevosegLayout.setVisible(true);
            }
        });

        add(tabs, filmLayout, kintlevosegLayout);
        kintlevosegLayout.setVisible(false);
    }

    private VerticalLayout createFilmLayout() {
        VerticalLayout layout = new VerticalLayout();
        layout.add(new H1("Filmek Listája"));

        filmGrid = new Grid<>(Film.class);
        filmGrid.setItems(getFilms());

        // Oszlopok megjelenítése
        filmGrid.setColumns("id", "cim", "kategoria", "megjelenesDatuma");

        // Állapot oszlop
        filmGrid.addColumn(film -> film.isBentVan() ? "Bent van" : "Nincs bent")
                .setHeader("Állapot");

        // Kivesz gomb
        filmGrid.addComponentColumn(this::createRentButton).setHeader("");

        // Törlés gomb
        filmGrid.addComponentColumn(this::createRemoveButton).setHeader("");

        layout.add(filmGrid);
        return layout;
    }

    private VerticalLayout createKintlevosegLayout() {
        VerticalLayout layout = new VerticalLayout();
        layout.add(new H1("Kintlevőség Lista"));


        kintlevosegEntityGrid = new Grid<>(kintlevoseg.class);

        kintlevosegEntityGrid.setColumns("filmcim", "nev", "telefonszam", "kivetelDatuma", "visszahozatal");
        kintlevosegEntityGrid.setItems(getkintlevoseg());

        layout.add(kintlevosegEntityGrid);
        return layout;
    }

    private List<Film> getFilms() {
        return filmService.getAllFilms();
    }

    private List<kintlevoseg> getkintlevoseg() {
        return kintlevosegService.getAllKintlevoseg();
    }

    private Button createRentButton(Film film) {
        Button rentButton = new Button("Kivesz");
        rentButton.addClickListener(event -> showKintlevosegForm(film));
        if (film.isBentVan()) {
            return rentButton;
        }
        return null;
    }

    private Button createRemoveButton(Film film) {
        Button removeButton = new Button("Törlés");
        removeButton.addClickListener(event -> removeFilm(film));
        return removeButton;
    }

    private void removeFilm(Film film) {
        filmService.removeFilm(film.getId());
        filmGrid.setItems(getFilms());
    }

    //kivételnél a kintlevesog kitöltő form:
    private void showKintlevosegForm(Film film) {

        Dialog dialog = new Dialog();
        dialog.setCloseOnEsc(true);
        dialog.setCloseOnOutsideClick(true);

        // Form elemek
        TextField nameField = new TextField("Név");
        TextField phoneField = new TextField("Telefonszám");
        DatePicker returnDateField = new DatePicker("Visszahozatal (dátum)");

        // A kivétel dátuma legyen a mai dátum
        LocalDate currentDate = LocalDate.now();
        returnDateField.setValue(currentDate);

        // A kintlevoseg példányosítása
        Button saveButton = new Button("Kivesz", event -> {
            kintlevoseg newKintlevoseg = new kintlevoseg();
            newKintlevoseg.setNev(nameField.getValue());
            newKintlevoseg.setTelefonszam(phoneField.getValue());
            newKintlevoseg.setKivetelDatuma(currentDate);
            newKintlevoseg.setVisszahozatal(returnDateField.getValue());

            // A film cím és ID mentése
            newKintlevoseg.setFilmId(film.getId());
            newKintlevoseg.setFilmcim(film.getCim());

            // A film frissítése
            film.setBentVan(false);
            filmService.SaveFilm(film);
            filmGrid.setItems(getFilms());

            // Kintlevoseg mentése
            kintlevosegService.saveKintlevoseg(newKintlevoseg);

            // Frissítjük a kintlevoseg gridet
            kintlevosegEntityGrid.setItems(getkintlevoseg());

            dialog.close();
        });

        dialog.add(nameField, phoneField, returnDateField, saveButton);
        dialog.open();
    }
}