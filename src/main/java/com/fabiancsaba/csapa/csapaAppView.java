package com.fabiancsaba.csapa;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route("")
public class csapaAppView extends VerticalLayout {

    private final filmService filmService;
    private Grid<Film> filmGrid;
    private Grid<kintlevoseg> kintlevosegEntityGrid;

    @Autowired
    public csapaAppView(filmService filmService) {
        this.filmService = filmService;

        // Tabok létrehozása
        Tab filmTab = new Tab("Filmek");
        Tab anotherTab = new Tab("Másik tábla");

        // Filmek layout
        VerticalLayout filmLayout = createFilmLayout();

        // Másik layout
        VerticalLayout anotherLayout = createAnotherEntityLayout();

        // Tabs kezelés
        Tabs tabs = new Tabs(filmTab, anotherTab);
        tabs.addSelectedChangeListener(event -> {
            if (event.getSelectedTab().equals(filmTab)) {
                filmLayout.setVisible(true);
                anotherLayout.setVisible(false);
            } else {
                filmLayout.setVisible(false);
                anotherLayout.setVisible(true);
            }
        });

        add(tabs, filmLayout, anotherLayout);
        anotherLayout.setVisible(false); // Csak a filmek tab látható kezdetben
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

    private VerticalLayout createAnotherEntityLayout() {
        VerticalLayout layout = new VerticalLayout();
        layout.add(new H1("Másik Tábla"));

        kintlevosegEntityGrid = new Grid<>(kintlevoseg.class);
        kintlevosegEntityGrid.setItems();


        layout.add(kintlevosegEntityGrid);
        return layout;
    }

    private List<Film> getFilms() {
        return filmService.getAllFilms();
    }

    private List<kintlevoseg> getAnotherEntities() {
        // Adatok lekérése a másik entitáshoz
        return anotherEntityService.getAllEntities();
    }

    private Button createRentButton(Film film) {
        Button rentButton = new Button("Kivesz");
        rentButton.addClickListener(event -> rentFilm(film));
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

    private void rentFilm(Film film) {
        film.setBentVan(false);
        filmService.SaveFilm(film);
        filmGrid.setItems(getFilms());
    }
}