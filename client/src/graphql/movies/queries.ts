import gql from "graphql-tag";

export const USER_MOVIES_QUERY = gql`
          query userMoviesQuery($id: Int!)
          {
              userMovies(id: $id) {
                title,
                posterLink
              }
          }`;


export const GET_CURRENT_MOVIE = gql`
          query GetCurrentMovie {
            currentMovie @client {
              title
              genres
              imdb
            }
          }`;

export const UPDATE_CURRENT_MOVIE = gql`
            mutation updateCurrentMovie($index: String!, $value: String!) {
              updateCurrentMovie(index: $index, value: $value) @client
            }`;

export const ADD_MOVIE = gql`
            mutation addMovie($title: String!, $genres: String!, $imdb: String!) {
              addMovie(title: $title, genre: $genres, IMDBLink: $imdb)
            }`; 