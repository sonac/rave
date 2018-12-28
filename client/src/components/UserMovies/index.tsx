import * as React from 'react';
import { Component } from 'react';
import { Query } from 'react-apollo';
import { Link } from 'react-router-dom';
import { RouteProps } from 'react-router';
import {USER_MOVIES_QUERY} from '../../graphql/movies/queries';
import { GET_CURRENT_USER } from '../Login';
import client from '../../graphql/apollo';

const styles = require('./styles.css');

interface CurrentUserQueryResult {
    currentUser: {
        username: String,
        token: String
    }
}

interface Props {}

interface State {}

export default class UserMovies extends Component<Props & RouteProps, State>  {

    componentDidUpdate() {
        console.log('asd')
    }

    render() {
        const data  = client.watchQuery<any>({ query: GET_CURRENT_USER}).currentResult()
        console.log(data)
        const username = 'test';
        return (
        <Query
            query={USER_MOVIES_QUERY}
            variables={{ username }}
        >
            {({ loading, error, data, refetch }) => {
            if (loading) return null;
            if (error) return `Error!: ${error}`;
            const add_img = '../../images/add.png';
            return (
                <div className={styles.poster_list}>
                    <Link to={`/add-movie`}><img src={add_img}/></Link>
                    {data.userMovies.map(x =>
                        <div key={x.title} className={styles.poster}><img src={x.posterLink}/></div>
                    )}
                </div>
            );
            }}
        </Query>
        )
    }

}