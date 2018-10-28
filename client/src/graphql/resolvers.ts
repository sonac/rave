import gql from "graphql-tag";

export const defaults = {
  currentMovie: {
    __typename: 'Movie',
    title: '', 
    genres: '', 
    imdb: ''}
};

export const resolvers = {
  Mutation: {
    updateCurrentMovie: (_, { index, value }, { cache }) => {
      const GET_CURRENT_MOVIE = gql`
          query GetCurrentMovie {
            currentMovie @client {
              title
              genres
              imdb
            }
          }`
      const previousState = cache.readQuery({ query: GET_CURRENT_MOVIE })
      const newData = {
        currentMovie: {
        ...previousState.currentMovie,
        [index]: value
      }}
      cache.writeQuery({ query: GET_CURRENT_MOVIE, data: newData })
      return null;
    }
  },
};
