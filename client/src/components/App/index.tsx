import * as React from 'react';
import { Component } from 'react';
import { Link, BrowserRouter as Router } from 'react-router-dom';
import Body from 'components/Body';

const styles = require('./styles.css');

export default class App extends Component {

    componentDidMount() {
    }

    render() {
        return (
            <Router>
                <div className={styles.app}>
                    <Link to='/'><h3>Home</h3></Link>
                    <Link to='/registration'><h4>Register</h4></Link>
                    <Link to='/login'><h4>Login</h4></Link>
                    <Body/>
                </div>
            </Router>
        );
    }
}
