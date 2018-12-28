import ApolloClient from "apollo-boost";
import { defaults, resolvers } from "./resolvers";
import { typeDefs } from "./schema";

const client = new ApolloClient({
    uri: 'http://localhost:8080/graphql',
    clientState: {
      defaults,
      resolvers: resolvers as any,
      typeDefs
    }
});

export default client;