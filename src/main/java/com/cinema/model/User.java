package com.cinema.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class User {
    @NonNull
    private int id;
    @NonNull
    private String name;
    @NonNull
    private String email;
    @NonNull
    private String phone;
}
