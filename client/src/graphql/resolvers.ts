import gql from "graphql-tag";

export const defaults = {
  currentMovie: {
    __typename: 'Movie',
    title: 'hello, apollo ', 
    genres: 'hey there', 
    imdb: 'yes'}
};

export const resolvers = {
  Mutation: {
    updateCurrentMovie: (_, { index, value }, { cache }) => {
      console.log(index, value);
    }
  },
};
