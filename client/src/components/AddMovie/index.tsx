import * as React from 'react';
import { Component } from "react";
import { Query, Mutation } from 'react-apollo';
import { GET_CURRENT_MOVIE, UPDATE_CURRENT_MOVIE, ADD_MOVIE } from '../../graphql/movies/queries';

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
                    {console.log(data)}
                            <div className={styles.inputs}>
                                <input type='text' placeholder='Title' onChange={(e) => {updateCurrentMovie({
                                                                                    variables: {
                                                                                    index: 'title',
                                                                                    value: e.target.value
                                                                                    }
                                                                    })}}/>
                                <input type='text' placeholder='Genres' onChange={(e) => {updateCurrentMovie({
                                                                                    variables: {
                                                                                    index: 'genres',
                                                                                    value: e.target.value
                                                                                    }
                                                                    })}}/>
                                <input type='text' placeholder='IMDB Link' onChange={(e) => {updateCurrentMovie({
                                                                                    variables: {
                                                                                    index: 'imdb',
                                                                                    value: e.target.value
                                                                                    }
                                                                    })}}/>
                            </div>
                            <Mutation mutation={ADD_MOVIE}>
                            { addMovie =>
                            <div className={styles.button}>
                                <button type="button" onClick={() => addMovie({ variables: {title: data.currentMovie.title, 
                                    genres: data.currentMovie.genres,
                                    imdb: data.currentMovie.imdb}})}>Add Movie</button> 
                            </div>
                            }
                            </Mutation>
                        </div>
                    }
                </Mutation>
            )}
        </Query>
    )
}

export default AddMovie;