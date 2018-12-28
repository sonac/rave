create schema rave;

create table rave.movies (
    id serial primary key,
    title varchar(256),
    genres varchar(256),
    imdb varchar(128),
    poster_link varchar(512),
    created_at timestamp
);

create table rave.users (
  id serial primary key,
  username varchar(32) unique,
  password varchar(256),
  email varchar(32) unique,
  created_at timestamp
);

create table rave.user_movies (
  id serial primary key,
  user_id integer references users(id),
  movie_id integer references movies(id),
  place varchar(64),
  comment varchar(256),
  created_at timestamp
);

insert into rave.movies (title, genres, imdb, poster_link, created_at)
values ('Meg', 'action', 'https://www.imdb.com/title/tt4779682',
        'https://m.media-amazon.com/images/M/MV5BMjg0MzA4MDE0N15BMl5BanBnXkFtZTgwMzk3MzAwNjM@._V1_.jpg',
        now());

insert into rave.movies (title, genres, imdb, poster_link, created_at)
values ('Godfather', 'drama', 'https://www.imdb.com/title/tt0068646',
        'https://m.media-amazon.com/images/M/MV5BM2MyNjYxNmUtYTAwNi00MTYxLWJmNWYtYzZlODY3ZTk3OTFlXkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_SY1000_CR0,0,704,1000_AL_.jpg',
        now());

insert into rave.movies (title, genres, imdb, poster_link, created_at)
values ('The Reader', 'drama', 'https://www.imdb.com/title/tt0976051',
        'https://m.media-amazon.com/images/M/MV5BMTM0NDQxNjA0N15BMl5BanBnXkFtZTcwNDUwMzcwMg@@._V1_.jpg',
        now());

insert into rave.users (username, password, email, created_at)
values ('test', 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.cXdl.WrokSmqk6uqFjJrCZnJ8oa5HORTVBCwrYf3UIpjrvuw', 'test@test', now());

insert into rave.user_movies (user_id, movie_id, place, comment, created_at)
values (1, 1, 'home', 'comment', now());

insert into rave.user_movies (user_id, movie_id, place, comment, created_at)
values (1, 2, 'home', 'comment', now());