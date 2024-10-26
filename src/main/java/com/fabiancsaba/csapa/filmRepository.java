package com.fabiancsaba.csapa;

import com.fabiancsaba.csapa.Film;

import org.springframework.data.jpa.repository.JpaRepository;

public interface filmRepository extends JpaRepository<Film, Long> {
}