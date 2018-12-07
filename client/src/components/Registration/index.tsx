import * as React from 'react';
import { Redirect } from 'react-router-dom'
import { Query, Mutation } from 'react-apollo';
import { GET_USER_INPUT, UPDATE_USER_INPUT, ADD_USER, GET_CURRENT_USER } from '../../graphql/movies/queries';

const styles = require('./styles.css');




const Registration = () => {

    return (
        <Query query={GET_USER_INPUT}>
            {({ data, client }) => (
                <Mutation mutation={UPDATE_USER_INPUT}>
                    { updateUserInput =>
                        <div className={styles.addMovie}>
                    {console.log(data)}
                            <div className={styles.inputs}>
                                <input type='text' placeholder='Username' onChange={(e) => {updateUserInput({
                                                                                    variables: {
                                                                                    index: 'username',
                                                                                    value: e.target.value
                                                                                    }
                                                                    })}}/>
                                <input type='password' placeholder='Password' onChange={(e) => {updateUserInput({
                                                                                    variables: {
                                                                                    index: 'password',
                                                                                    value: e.target.value
                                                                                    }
                                                                    })}}/>
                                <input type='text' placeholder='Email' onChange={(e) => {updateUserInput({
                                                                                    variables: {
                                                                                    index: 'eMail',
                                                                                    value: e.target.value
                                                                                    }
                                                                    })}}/>
                            </div>
                            <Mutation 
                                mutation={ADD_USER}
                                update={(cache, { data: { addUser } }) => {
                                    cache.writeQuery({
                                    query: GET_CURRENT_USER,
                                    data: { currentUser: {...addUser, __typename: 'CurrentUser'} }
                                    });
                                    cache.writeQuery({
                                    query: GET_USER_INPUT,
                                    data: { userInput: {__typename: 'User', username: '', password: '', eMail: ''} }
                                    });
                            }}>
                            { addUser =>
                            <div className={styles.button}>
                                <button type="button" onClick={() => addUser({ variables: {username: data.userInput.username, 
                                    password: data.userInput.password,
                                    eMail: data.userInput.eMail}}).then(() => window.location.href = '/')}>Register</button> 
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

export default Registration;