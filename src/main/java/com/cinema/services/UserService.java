package com.cinema.services;

import com.cinema.model.User;
import com.cinema.repository.UserDbStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserDbStore userDbStore;

    public User add(User user) {
        return userDbStore.add(user);
    }
}
