package com.cinema.repository;

import com.cinema.model.Film;
import lombok.RequiredArgsConstructor;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
@RequiredArgsConstructor
public class FilmDbStore {
    private final BasicDataSource pool;

    public Film add(Film film) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("INSERT INTO films(name) VALUES (?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, film.getName());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    film.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return film;
    }

    public List<Film> findAll() {
        List<Film> films = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM films")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    films.add(new Film(it.getInt("id"), it.getString("name")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return films;
    }

    public Optional<Film> findById(int id) {
        Optional<Film> session = Optional.empty();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM films WHERE id = ? order by id")
        ) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    session = Optional.of(new Film(it.getInt("id"), it.getString("name")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return session;
    }

    public Set<Integer> findAllRows() {
        Set<Integer> films = new HashSet<>();
        List<Map<Integer, Integer>> listMap = allCinema();
        for (Map<Integer, Integer> map : listMap) {
            Collection<Integer> values = map.values();
            films.addAll(values);
        }
       /* listMap.stream()
                .map(integerIntegerMap -> films.addAll(new HashSet<>(integerIntegerMap.values())));*/
        return films;
    }

    public List<Map<Integer, Integer>> findAlFreeTickets() {
        List<Map<Integer, Integer>> listMap = allCinema();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT place_row, place_cell FROM ticket order by place_row")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    int placeRow = it.getInt("place_row");
                    int placeCell = it.getInt("place_cell");
                    listMap.forEach(listElement -> {
                        if (listElement.containsKey(placeCell) && listElement.containsValue(placeRow)) {
                            listElement.remove(placeCell, placeRow);
                        }
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listMap;
    }

    private List<Map<Integer, Integer>> allCinema() {
        List<Map<Integer, Integer>> lisMap = new ArrayList<>();
        Map<Integer, Integer> map = new ConcurrentHashMap<>();
        for (Integer i = 1; i <= 3; i++) {
            for (int j = 1; j <= 10; j++) {
                map.put(j, i);
            }
            lisMap.add(map);
            map = new ConcurrentHashMap<>();
        }
        return lisMap;
    }
}
