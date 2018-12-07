import gql from "graphql-tag";
import { LOGIN, GET_CURRENT_USER } from './movies/queries'

export const defaults = {
  movieInput: {
    __typename: 'Movie',
    title: '', 
    genres: '', 
    imdb: ''
  },
  userInput: {
    __typename: 'User',
    username: '',
    password: '',
    eMail: ''
  },
  currentUser: {
    __typename: 'CurrentUser',
    username: null,
    eMail: null,
    token: null
  },
  loginInput: {
    __typename: 'Login',
    username: null,
    password: null
  },
  loginError: {
    __typename: 'LoginError',
    error: null
  }
};

export const resolvers = {
  Mutation: {
    updateCurrentMovie: (_, { index, value }, { cache }) => {
      const GET_CURRENT_MOVIE = gql`
          query GetCurrentMovie {
            movieInput @client {
              title
              genres
              imdb
            }
          }`
      const previousState = cache.readQuery({ query: GET_CURRENT_MOVIE })
      const newData = {
        movieInput: {
        ...previousState.movieInput,
        [index]: value
      }}
      cache.writeQuery({ query: GET_CURRENT_MOVIE, data: newData })
      return null;
    },
    updateUserInput: (_, { index, value }, { cache }) => {
      const previousState = cache.readQuery({ query: GET_CURRENT_USER })
      const newData = {
        userInput: {
        ...previousState.userInput,
        [index]: value
      }}
      cache.writeQuery({ query: GET_CURRENT_USER, data: newData })
      return null;
    },
    updateLoginInput: (_, { index, value }, { cache }) => {
      const GET_CURRENT_LOGIN = gql`
        query GetCurrentLogin {
          loginInput @client {
            username
            password
          }
        }`
      const previousState = cache.readQuery({ query: GET_CURRENT_LOGIN })
      const newData = {
        loginInput: {
        ...previousState.loginInput,
        [index]: value
      }}
      cache.writeQuery({ query: GET_CURRENT_LOGIN, data: newData })
      return null;
    },
    updateCurrentUser: (_, { username, password }, { cache }) => {
      const login = cache.readQuery({ query: LOGIN, variables: {username: username, password: password} })
      if (login === null) {
        cache.writeQuery({
          query: gql`
          query GetLoginError {
            loginError @client {
              error
            }
          }`,
          data: {
            loginError: {
              error: 'wrong loing data'
            }
          }
        })
      }
      else {
        const newData = {
          currentUser: {
          ...login
        }}
        cache.writeQuery({ query: GET_CURRENT_USER, data: newData })
      }
      return null;
    }
  },
};
