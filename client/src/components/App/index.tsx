import * as React from 'react';
import { Component } from 'react';
import Body from 'components/Body';

const styles = require('./styles.css');

export default class App extends Component {

    componentDidMount() {
    }

    render() {
        return (
                <div className={styles.app}>
                    <h3>Sholom, wolrd!</h3>
                    <Body/>
                </div>
        );
    }
}
