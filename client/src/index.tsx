import * as React from "react";
import * as ReactDOM from "react-dom";
import { ApolloProvider } from 'react-apollo'
import App from 'components/App/index';
import client from "./graphql/apollo";

ReactDOM.render(
        <ApolloProvider client={client}>
            <App />
        </ApolloProvider>,
    document.getElementById('root')
)