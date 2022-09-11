package com.cinema.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Ticket {
    @NonNull
    private int id;
    @NonNull
    private Film film;
    @NonNull
    private int placeRow;
    @NonNull
    private int placeCell;
    @NonNull
    private User user;
}
