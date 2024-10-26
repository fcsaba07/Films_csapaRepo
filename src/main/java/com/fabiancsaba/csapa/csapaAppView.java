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

        // Film tab
        VerticalLayout filmLayout = createFilmLayout();

        // Kintlevoseg tab
        VerticalLayout kintlevosegLayout = createKintlevosegLayout();

        // Tabs kezelés
        Tabs tabs = new Tabs(filmTab, kintlevosegTab);
        tabs.addSelectedChangeListener(event -> {
            if (event.getSelectedTab().equals(filmTab)) {
                filmLayout.setVisible(true);
                kintlevosegLayout.setVisible(false);
                filmGrid.setItems(getFilms());
            } else {
                filmLayout.setVisible(false);
                kintlevosegLayout.setVisible(true);
                kintlevosegEntityGrid.setItems(getkintlevoseg());
            }
        });

        add(tabs, filmLayout, kintlevosegLayout);
        kintlevosegLayout.setVisible(false);
    }

    //layoutok létrehozása

    private VerticalLayout createFilmLayout() {
        VerticalLayout layout = new VerticalLayout();
        layout.add(new H1("Filmek Listája"));

        filmGrid = new Grid<>(Film.class);
        filmGrid.setItems(getFilms());

        filmGrid.setColumns("id", "cim", "kategoria", "megjelenesDatuma", "mennyiseg");
        filmGrid.addColumn(film -> film.isBentVan() ? "Bent van" : "Nincs bent").setHeader("Állapot");

        // Kivesz gomb
        filmGrid.addComponentColumn(this::createRentButton).setHeader("");

        // Törlés gomb
        filmGrid.addComponentColumn(this::createRemoveButton).setHeader("");

        layout.add(filmGrid);
        return layout;
    }

    private VerticalLayout createKintlevosegLayout() {
        VerticalLayout layout = new VerticalLayout();
        layout.add(new H1("Kintlevőségek"));


        kintlevosegEntityGrid = new Grid<>(kintlevoseg.class);
        kintlevosegEntityGrid.setItems(getkintlevoseg());

        kintlevosegEntityGrid.setColumns("filmcim", "nev", "telefonszam", "kivetelDatuma", "visszahozatal");
        kintlevosegEntityGrid.addComponentColumn(this::createReturnButton).setHeader("Visszahoz");
        layout.add(kintlevosegEntityGrid);
        return layout;
    }

    //Gombok létrehozása

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

    private Button createReturnButton(kintlevoseg kintlevoseg) {
        Button returnButton = new Button("visszahoz");
        returnButton.addClickListener(event -> returnFilm(kintlevoseg));
        return returnButton;
    }

    //Eventek a gombok mögött

    private void removeFilm(Film film) {
        List<Long> kintlevosegFilmIds = kintlevosegService.getAllFilmIdsInKintlevoseg();

        if (kintlevosegFilmIds.contains(film.getId())) {
            Notification.show("Hiba: A filmet kivették. Nem törölhető amíg nem kerül vissza", 3000, Notification.Position.MIDDLE);
        } else {
            filmService.removeFilm(film.getId());
            filmGrid.setItems(getFilms());
        }
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

            kintlevosegService.saveKintlevoseg(newKintlevoseg);

            // A film frissítése
            film.setMennyiseg(film.getMennyiseg()-1);
            if (film.getMennyiseg()==0) {
                film.setBentVan(false);
                filmService.SaveFilm(film);
                filmGrid.setItems(getFilms());
            } else   filmService.SaveFilm(film);
            filmGrid.setItems(getFilms());

            // Frissítjük a kintlevoseg gridet
            kintlevosegEntityGrid.setItems(getkintlevoseg());
            dialog.close();
        });
        dialog.add(nameField, phoneField, returnDateField, saveButton);
        dialog.open();
    }

    //listák lekérdezése

    private List<Film> getFilms() {
        return filmService.getAllFilms();
    }

    private List<kintlevoseg> getkintlevoseg() {
        return kintlevosegService.getAllKintlevoseg();
    }
}