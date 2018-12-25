import * as React from 'react';
import { Mutation } from 'react-apollo';
import { RouteProps } from 'react-router';
import gql from 'graphql-tag';
import { AUTH_TOKEN } from '../../constants';

const styles = require('./styles.css');

const REGISTER_MUTATION = gql`
    mutation addUser($username: String!, $password: String!, $email: String!) {
        addUser(username: $username, password: $password, email: $email) {
            token
        }
    }
`; 

const LOGIN_MUTATION = gql`
    mutation Authenticate($username: String!, $password: String!){
        authenticate(username: $username, password: $password) {
            token
        }
    }
`;

interface Props {}

interface State {
    login: boolean,
    email: string,
    password: string,
    username: string
}

export default class Login extends React.Component<Props & RouteProps, State> {
    state = {
        login: true, //switch between login and register
        email: '',
        password: '',
        username: '',
    }

    render() {
        const { login, email, password, username } = this.state;
        return (
            <div className={styles.wrapper}>
                <div className={styles.loginForm}>
                    <h4 className={styles.title}>{login ? 'Sign In' : 'Sign Up'}</h4>
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
                        onCompleted={data => this._confirm(data)}
                    >
                        {mutation => (
                            <button type='button' onClick={() => mutation()}>{login ? 'Login' : 'Register'}</button>
                        )}
                </Mutation>
                </div>
                <div className={styles.pointerButton} onClick={() => this.setState({ login: !login })}>
                        <a>{login ? 'Need to create an account?' : 'Already have an account?'}</a>
                </div>
            </div>

        )
    }

    _confirm = async data => {
        const { token } = this.state.login ? data.authenticate : data.addUser;
        fetch(`/set-cookie?token=${token}`)
            .then(this.props.history.push('/'))
    }

    _saveUserData = token => {
        localStorage.setItem(AUTH_TOKEN, token)
    }

}