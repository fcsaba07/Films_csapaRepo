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
        Grid<Film> grid = new Grid<>(Film.class);
        grid.setItems(getFilms());

        // Oszlopok megjelenítése
        grid.setColumns("id", "cim", "kategoria", "megjelenesDatuma", "bentVan");

        grid.addComponentColumn(this::createRemoveButton).setHeader("Kivesz");

        add(grid);
    }

    private List<Film> getFilms() {
        return filmService.getAllFilms();
    }

    private Button createRemoveButton(Film film) {
        if (film.isBentVan()) { // Csak ha bentVan true
            Button removeButton = new Button("Kivesz");
            removeButton.addClickListener(event -> removeFilm(film));
            return removeButton;
        }
        return null;
    }
    private void removeFilm(Film film) {
        filmService.removeFilm(film.getId());
        grid.setItems(getFilms());
    }

}

