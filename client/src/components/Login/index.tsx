import * as React from 'react';
import { Mutation } from 'react-apollo';
import { RouteProps } from 'react-router';
import gql from 'graphql-tag';

const styles = require('./styles.css');

const REGISTER_MUTATION = gql`
    mutation addUser($username: String!, $password: String!, $email: String!) {
        addUser(username: $username, password: $password, email: $email) {
            username
            token
        }
    }
`; 

const LOGIN_MUTATION = gql`
    mutation authenticate($email: String!, $password: String!){
        authenticate(email: $email, password: $password) {
            username
            token
        }
    }
`;

export const GET_CURRENT_USER = gql`
    query getCurrentUser {
        currentUser @client {
        username
        token
        }
    }
`;

interface Props {}

interface State {
    login: boolean,
    email: string,
    password: string,
    username: string,
    token: String,
    error: String
}

export default class Login extends React.Component<Props & RouteProps, State> {
    state = {
        login: true, //switch between login and register
        email: '',
        password: '',
        username: '',
        token: '',
        error: '',
    }

    updateCacheAfterAuth = async (store, userData) => {
        const { username, token } = this.state.login ? userData.authenticate : userData.addUser;
        const data = store.readQuery({ query: GET_CURRENT_USER })
        const newUser = {
            currentUser: {
            ...data.currentUser,
            username: username,
            token: token
            }}
        store.writeQuery({ query: GET_CURRENT_USER, data: newUser })
    }

    confirm = async data => {
        const token = this.state.login ? data.authenticate.token : data.addUser.token;
        this.setState({ token: token});
        await fetch(`/set-cookie?token=${token}`)
        this.props.history.push('/')
    }

    handleError = async (err) => {
        if (err.includes('Username or password')) {
            this.setState({ error: 'Username or password is incorrect'})
        }
        if (err.includes('User with such email')) {
            this.setState({ error: 'User with such email or name already exists' })
        }
    }

    render() {
        const { login, email, password, username, token, error } = this.state;
        return (
            <div className={styles.wrapper}>
                <div className={styles.loginForm}>
                    <h4 className={styles.title}>{login ? 'Sign In' : 'Sign Up'}</h4>
                    <div className={styles.errorMsg}>
                        {error}
                    </div>
                    {!login && (
                        <input
                            value={username}
                            onChange={e => this.setState({ username: e.target.value })}
                            type='text'
                            placeholder='Name'
                        />
                    )}
                    <input
                        value={email}
                        onChange={e => this.setState({ email: e.target.value })}
                        type='text'
                        placeholder='Email'
                    />
                    <input
                        value={password}
                        onChange={e => this.setState({ password: e.target.value })}
                        type='password'
                        placeholder='Password'
                    />
                </div>
                <div className={styles.button}>
                        <Mutation
                            mutation={login ? LOGIN_MUTATION : REGISTER_MUTATION}
                            variables={{ password, username, email }}
                            update={( cache, { data} ) => 
                                this.updateCacheAfterAuth(cache, data)
                            }
                            onError={err => {
                                this.handleError(err.message)
                            }}
                            onCompleted={data => this.confirm(data)}
                        >
                            {mutation => (
                                <button type='button' onClick={() => mutation()}>
                                    {login ? 'Login' : 'Register'}
                                </button>
                            )} 
                </Mutation>
                </div>
                <div className={styles.pointerButton} onClick={() => this.setState({ login: !login })}>
                        <a>{login ? 'Need to create an account?' : 'Already have an account?'}</a>
                </div>
            </div>

        )
    }

}