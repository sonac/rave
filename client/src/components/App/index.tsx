import * as React from 'react';
import { Component } from 'react';
import { RouteProps } from 'react-router';
import { Link, BrowserRouter as Router } from 'react-router-dom';
import Body from 'components/Body';

const styles = require('./styles.css');

interface Props {}

interface State {
    username: string
}

export default class App extends Component<Props & RouteProps, State> {

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
            .then(data => this.setState({ username: data }))
    }

    logout() {
        fetch('/logout')
            .then(this.props.history.push('/'))
    }

    render() {
        return (
            <Router>
                <div className={styles.app}>
                    <div className={styles.home}><Link to='/'><h3>Home</h3></Link></div>
                    {this.state.username ? (
                        <div className={styles.userWrapper}>
                            <div className={styles.welcome}><h3>Welcome, {this.state.username}</h3></div>
                            <div className={styles.logout} onClick={this.logout}><h4>Logout?</h4></div>
                        </div>
                     ) : (
                        <div className={styles.auth}><Link to='/login'><h3>Sign Up</h3></Link></div>
                     )}
                    <div className={styles.body}><Body/></div>
                </div>
            </Router>
        );
    }
}
