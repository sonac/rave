import * as React from 'react';
import {Component} from 'react';
import { Query } from 'react-apollo';
import { Link } from 'react-router-dom';
import {IdInputProps, UserMoviesResponse} from 'graphql/movies/types';
import {USER_MOVIES_QUERY} from '../../graphql/movies/queries';

const styles = require('./styles.css');

const UserMovies = ({ userId }) => {
    const id = userId;
    return (
    <Query
        query={USER_MOVIES_QUERY}
        variables={{ id }}
    >
        {({ loading, error, data, refetch, networkStatus }) => {
        if (networkStatus === 4) return "Refetching!";
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

export default UserMovies