import * as React from 'react';
import { Query, Mutation } from 'react-apollo';
import { GET_CURRENT_LOGIN, UPDATE_LOGIN_INPUT, UPDATE_CURRENT_USER } from '../../graphql/movies/queries';

const styles = require('./styles.css');




const Login = () => {

    return (
        <Query query={GET_CURRENT_LOGIN}>
            {({ data, client }) => (
                <Mutation mutation={UPDATE_LOGIN_INPUT}>
                    { updateLoginInput =>
                        <div className={styles.addMovie}>
                    {console.log(data)}
                            <div className={styles.inputs}>
                                <input type='text' placeholder='Login' onChange={(e) => {updateLoginInput({
                                                                                    variables: {
                                                                                    index: 'username',
                                                                                    value: e.target.value
                                                                                    }
                                                                    })}}/>
                                <input type='text' placeholder='Password' onChange={(e) => {updateLoginInput({
                                                                                    variables: {
                                                                                    index: 'password',
                                                                                    value: e.target.value
                                                                                    }
                                                                    })}}/>
                            </div>
                            <Mutation mutation={UPDATE_CURRENT_USER}>
                            { updateCurrentUser =>
                            <div className={styles.button}>
                                <button type="button" onClick={() => updateCurrentUser({ variables: {username: data.loginInput.username, 
                                    password: data.loginInput.password}})}>Login</button> 
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

export default Login;