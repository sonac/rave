import * as React from 'react';
import { Component } from 'react';
import { RouteProps } from 'react-router';
import { Link, BrowserRouter as Router } from 'react-router-dom';
import Body from 'components/Body';
import Header from 'components/Header';
import client from '../../graphql/apollo';

const styles = require('./styles.css');

interface Props {}

interface State {
    username: string
}

export default class App extends Component<Props & RouteProps, State> {

    render() {
        return (
            <Router>
                <div className={styles.app}>
                    <div className={styles.header}><Header/></div>
                    <div className={styles.body}><Body/></div>
                </div>
            </Router>
        );
    }

}
