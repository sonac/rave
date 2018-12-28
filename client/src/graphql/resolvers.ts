import gql from "graphql-tag";

export const defaults = {
  movieInput: {
    __typename: 'Movie',
    title: '', 
    genres: '', 
    imdb: ''
  },
  currentUser: {
    __typename: 'CurrentUser',
    username: '',
    token: ''
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
    updateCurrentUser: (_, { username, token }, { cache }) => {
      const GET_CURRENT_USER = gql`
        query getCurrentUser {
          currentUser @cleint {
            username
            token
          }
        }`
      const previousState = cache.readQuery({ query: GET_CURRENT_USER })
      const newData = {
        currentUser: {
          username: username,
          token: token
        }}
      cache.writeQuery({ query: GET_CURRENT_USER, data: newData})
      return null;
    }
  },
};
