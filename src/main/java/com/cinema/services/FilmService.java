package com.cinema.services;

import com.cinema.model.Film;
import com.cinema.repository.FilmDbStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmDbStore filmDbStore;

    public Film add(Film film) {
        return filmDbStore.add(film);
    }

    public List<Film> findAll() {
        return filmDbStore.findAll();
    }

    public Optional<Film> findById(int id) {
        return filmDbStore.findById(id);
    }

    public Set<Integer> findAllRows() {
        return filmDbStore.findAllRows();
    }

    public List<Map<Integer, Integer>> findAlFreeTickets() {
        return filmDbStore.findAlFreeTickets();
    }
}
