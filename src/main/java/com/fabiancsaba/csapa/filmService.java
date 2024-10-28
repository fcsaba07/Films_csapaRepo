package com.fabiancsaba.csapa;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class filmService {

    private final filmRepository filmRepository;

    public filmService(filmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    public List<Film> getAllFilms() {
        return filmRepository.findAll();
    }

    public void removeFilm(Long id) {
        filmRepository.deleteById(id);
    }

    public void SaveFilm(Film film) {
        filmRepository.save(film);
    }
    public Film getFIlmById(Long id) {
        Film film = filmRepository.findById(id).get();
        return film;
    }

}


