package com.cinema.model;

import lombok.*;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Film {
    @NonNull
    private int id;
    @NonNull
    private String name;
}
