package com.fabiancsaba.csapa;

import com.vaadin.flow.component.avatar.AvatarGroup;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route("")
public class csapaAppView extends VerticalLayout {

    private final filmService filmService;
    private Grid<Film> grid;

    @Autowired
    public csapaAppView(filmService filmService) {
        this.filmService = filmService;

        add(new H1("Filmek Listája"));

        // Grid létrehozása a filmek megjelenítésére
        grid = new Grid<>(Film.class);
        grid.setItems(getFilms());


        // Oszlopok megjelenítése
        grid.setColumns("id", "cim", "kategoria", "megjelenesDatuma");

        //bent van-e a film oszlop
        grid.addColumn(film -> film.isBentVan() ? "Bent van" : "Nincs bent").setHeader("Állapot");

        //kivesz gomb
        grid.addComponentColumn(this::createRentButton).setHeader("");

        //törlés gomb
        grid.addComponentColumn(this::createRemoveButton).setHeader("");

        add(grid);
    }

    private List<Film> getFilms() {
        return filmService.getAllFilms();
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
        grid.setItems(getFilms());
    }

    private void rentFilm(Film film) {
        film.setBentVan(false);
        filmService.SaveFilm(film);
        grid.setItems(getFilms());
    }

}

