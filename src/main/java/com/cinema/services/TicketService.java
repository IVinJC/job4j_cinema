package com.cinema.services;

import com.cinema.model.Ticket;
import com.cinema.repository.TicketDbStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketDbStore ticketDbStore;

    public Optional<Ticket> add(Ticket ticket) {
        return ticketDbStore.add(ticket);
    }

    public Optional<Ticket> findById(int id) {
        return ticketDbStore.findById(id);
    }
}
