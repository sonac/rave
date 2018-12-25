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
            movieInput @client {
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

export const GET_USER_INPUT = gql`
            query GetUserInput {
              userInput @client {
                username
                password
                eMail
              }
}`;

export const UPDATE_USER_INPUT = gql`
            mutation updateUserInput($index: String!, $value: String!) {
              updateUserInput(index: $index, value: $value) @client
            }`;

export const GET_CURRENT_USER = gql`
            query GetCurrentUser {
              currentUser @client {
                username
                eMail
                token
              }
            }`;

export const UPDATE_LOGIN_INPUT = gql`
            mutation updateLoginInput($index: String!, $value: String!) {
              updateLoginInput(index: $index, value: $value) @client
            }`;

export const GET_CURRENT_LOGIN = gql`
            query GetCurrentLogin {
              loginInput @client {
                username
                password
              }
            }`;

export const UPDATE_CURRENT_USER = gql`
            mutation updateCurrentUser($username: String!, $password: String!) {
              updateCurrentUser(username: $username, password: $password) @client
            }`;