CREATE TABLE if not exists users (
                       id SERIAL PRIMARY KEY,
                       username VARCHAR NOT NULL,
                       email VARCHAR NOT NULL,
                       phone VARCHAR NOT NULL
);

CREATE TABLE if not exists films (
                          id SERIAL PRIMARY KEY,
                          name text
);

CREATE TABLE if not exists ticket (
                        id SERIAL PRIMARY KEY,
                        session_id INT NOT NULL REFERENCES films(id) unique ,
                        place_row INT NOT NULL unique ,
                        place_cell INT NOT NULL unique ,
                        user_id INT NOT NULL REFERENCES users(id)
);