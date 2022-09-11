package com.cinema.controller;

import com.cinema.model.Film;
import com.cinema.model.Ticket;
import com.cinema.model.User;
import com.cinema.services.FilmService;
import com.cinema.services.TicketService;
import com.cinema.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;
@Slf4j
@Controller
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;
    private final TicketService ticketService;
    private final UserService userService;
    private final ObjectMapper objectMapper;

    @GetMapping({"/", "/index"})
    public String start(Model model, @ModelAttribute("ticket") Ticket ticket) {
        model.addAttribute("films", filmService.findAll());
        model.addAttribute("listMap", filmService.findAlFreeTickets());
        model.addAttribute("rows", filmService.findAllRows());
        return "index";
    }

    @PostMapping("/createticket")
    public String sendFilm(@ModelAttribute("ticket") Ticket ticket) {
        User ticketUser = ticket.getUser();
        User user = userService.add(ticketUser);
        ticketUser.setId(user.getId());
        Optional<Film> filmOptional = filmService.findById(ticket.getFilm().getId());
        Film film = filmOptional.orElse(null);
        assert film != null;
        ticket.setFilm(film);
        Optional<Ticket> ticketOptional = ticketService.add(ticket);
        Ticket ticketOrElse = ticketOptional.orElse(null);
        assert ticketOrElse != null;
        int id = ticketOrElse.getId();
        return "redirect:/result?id=" + id;
    }

    @GetMapping("/result")
    public String result(Model model, @RequestParam(name = "id", required = false) String id) {
        Ticket ticket = ticketService.findById(Integer.parseInt(id)).orElse(null);
        assert ticket != null;
        model.addAttribute("ticket", ticket);
        return "result";
    }

    @ExceptionHandler(value = {Exception.class})
    public void handleException(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(new HashMap<>() { {
            put("message", "Some of fields empty");
            put("details", e.getMessage());
        }}));
        log.error(e.getMessage());
    }
}
