package com.fabiancsaba.csapa;

import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route("")
public class csapaAppView extends VerticalLayout {

    private final filmService filmService;
    private final kintlevosegService kintlevosegService;
    private final filmLayout filmLayout;
    private final kintlevosegLayout kintlevosegLayout;

    @Autowired
    public csapaAppView(filmService filmService, kintlevosegService kintlevosegService) {
        this.filmService = filmService;
        this.kintlevosegService = kintlevosegService;

        // Inicializáljuk a layoutokat
        filmLayout = new filmLayout(filmService, kintlevosegService);
        kintlevosegLayout = new kintlevosegLayout(kintlevosegService, filmService);

        Tab filmTab = new Tab("Filmek");
        Tab kintlevosegTab = new Tab("Kintlevoseg");
        Tabs tabs = new Tabs(filmTab, kintlevosegTab);

        tabs.addSelectedChangeListener(event -> {
            if (event.getSelectedTab().equals(filmTab)) {
                filmLayout.setVisible(true);
                kintlevosegLayout.setVisible(false);
                filmLayout.refreshfilms();

            } else {
                filmLayout.setVisible(false);
                kintlevosegLayout.setVisible(true);
                kintlevosegLayout.refreshKintlevoseg();
            }
        });

        add(tabs, filmLayout, kintlevosegLayout);
        kintlevosegLayout.setVisible(false);
    }

    private List<Film> getFilms() {
        return filmService.getAllFilms();
    }

    private List<kintlevoseg> getkintlevoseg() {
        return kintlevosegService.getAllKintlevoseg();
    }
}