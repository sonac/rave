import * as React from 'react';
import {Component, Props} from 'react';
import { Route, BrowserRouter as Router} from 'react-router-dom'
import UserMovies from "../UserMovies";
import AddMovie from 'components/AddMovie'

const styles = require('./styles.css') 

export default function Body() {

       return  (
        <Router>
            <div className={styles.body}>
                <Route exact path="/" render={(props) => <UserMovies {...props} userId={1} />} />
                <Route exact path="/add-movie" component={AddMovie} />
            </div>
         </Router>
        );
}