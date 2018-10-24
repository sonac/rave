import * as React from 'react';
import { Component } from "react";
import { Query, Mutation } from 'react-apollo';
import { GET_CURRENT_MOVIE, UPDATE_CURRENT_MOVIE } from '../../graphql/movies/queries';

const styles = require('./styles.css');




const AddMovie = () => {

    function handleChange(e, inp) {
        console.log(inp);
    }
    
    function handleClick() {
        console.log('click')
    }

    return (
        <Query query={GET_CURRENT_MOVIE}>
            {({ data, client }) => (
                <Mutation mutation={UPDATE_CURRENT_MOVIE}>
                    { updateCurrentMovie =>
                        <div className={styles.addMovie}>
                            <div className={styles.inputs}>
                            {console.log(data)}
                                <input type='text' placeholder='Title' onChange={(e) => {handleChange(e, 'title')}}/>
                                <input type='text' placeholder='Genres' onChange={(e) => {handleChange(e, 'genres')}}/>
                                <input type='text' placeholder='IMDB Link' onChange={(e) => {handleChange(e, 'imdb')}}/>
                            </div>
                            <div className={styles.button}>
                                <button type="button" onClick={() => updateCurrentMovie({
                                                                                    variables: {
                                                                                    index: 'teamAName',
                                                                                    value: 'e.target.value'
                                                                                    }
                                                                    })}>Add Movie</button> 
                            </div>
                        </div>
                    }
                </Mutation>
            )}
        </Query>
    )
}

export default AddMovie;