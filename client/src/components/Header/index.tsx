import * as React from 'react';
import { Component } from 'react';
import { RouteProps } from 'react-router';
import { Link, BrowserRouter as Router } from 'react-router-dom';
import { GET_CURRENT_USER } from 'components/Login';
import client from '../../graphql/apollo';
import { Query } from 'react-apollo';

const styles = require('./styles.css');

interface Props {}

interface State {
    username: string
}

export default class Header extends Component<Props & RouteProps, State> {

    constructor(props) { 
        super(props);
        
        this.state = {
            username: '',
        };

        this.logout = this.logout.bind(this);
    }

    componentDidMount() {
        fetch('/check-auth')
            .then(response => response.json())
            .then(data => {
                this.setState({ username: data })
                this.updateCacheAfterAuth(client, data)
            })
        
    }

    updateCacheAfterAuth = async (store, userData) => {
        const data = store.readQuery({ query: GET_CURRENT_USER })
        const newUser = {
            currentUser: {
            ...data.currentUser,
            username: userData
            }}
        store.writeQuery({ query: GET_CURRENT_USER, data: newUser })
    }

    clearUserFromCache = (store) => {
        const data = store.readQuery({query: GET_CURRENT_USER});
        const emptyUser = {
            currentUser: {
            ...data.currentUser,
            username: '',
            token: ''
          }}
        client.writeQuery({query: GET_CURRENT_USER, data: emptyUser});
    }

    logout() {
        fetch('/logout')
            .then(() =>{
                this.setState({ username: ''});
                this.clearUserFromCache(client);
            })
    }

    render() {
        const username = this.state.username;
        return (
            <div className={styles.app}>
                <div className={styles.home}><Link to='/'><h3>Home</h3></Link></div>
                <Query query={GET_CURRENT_USER}>
                {({ loading, error, data }) => {
                    if (!username && !data.currentUser.username) return (
                        <div className={styles.auth}><Link to='/login'><h3>Sign In</h3></Link></div>
                    )
                    return (
                        <div className={styles.userWrapper}>
                            <div className={styles.welcome}>
                                <h3>Welcome, {username ? username : data.currentUser.username}</h3>
                            </div>
                            <div className={styles.logout} onClick={this.logout}><h4>Logout?</h4></div>
                        </div>
                    )
                }}
                    </Query>
            </div>
        );
    }

}
