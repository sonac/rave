import * as React from "react";
import * as ReactDOM from "react-dom";
import { Route, BrowserRouter as Router } from 'react-router-dom';
import { ApolloProvider } from 'react-apollo'
import App from 'components/App/index';
import client from "./graphql/apollo";

ReactDOM.render(
        <Router>
            <ApolloProvider client={client}>
                <Route path="/" component={App}/>
            </ApolloProvider>
        </Router>,
    document.getElementById('root')
)