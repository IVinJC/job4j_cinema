package com.cinema.repository;

import com.cinema.model.Film;
import com.cinema.model.Ticket;
import com.cinema.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;
@Slf4j
@Repository
@RequiredArgsConstructor
public class TicketDbStore {
    private final BasicDataSource pool;

    public Optional<Ticket> add(Ticket ticket) {
        Optional<Ticket> ticketOptional = Optional.empty();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "INSERT INTO ticket(session_id, place_row, place_cell, user_id) VALUES (?,?,?,?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setInt(1, ticket.getFilm().getId());
            ps.setInt(2, ticket.getPlaceRow());
            ps.setInt(3, ticket.getPlaceCell());
            ps.setInt(4, ticket.getUser().getId());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    ticket.setId(id.getInt(1));
                    ticketOptional = Optional.of(ticket);
                }
            }
        } catch (Exception e) {
            log.error("Ошибка доступа к базе данных", e);
        }
        return ticketOptional;
    }

    public Optional<Ticket> findById(int id) {
        Optional<Ticket> ticket = Optional.empty();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "SELECT * FROM ticket t "
                             + "join films f on f.id = t.session_id "
                             + "join users u on u.id = t.user_id "
                             + "WHERE t.id = ?")
        ) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    Ticket tiket = new Ticket();
                    tiket.setId(id);
                    tiket.setFilm(new Film(it.getInt("session_id"), it.getString("name")));
                    tiket.setPlaceRow(it.getInt("place_row"));
                    tiket.setPlaceCell(it.getInt("place_cell"));
                    tiket.setUser(new User(it.getInt("user_id"),
                            it.getString("username"),
                            it.getString("email"),
                            it.getString("phone")));
                    ticket = Optional.of(tiket);
                }
            }
        } catch (Exception e) {
            log.error("Ошибка доступа к базе данных", e);
        }
        return ticket;
    }
}
