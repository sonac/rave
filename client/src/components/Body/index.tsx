import * as React from 'react';
import {Component, Props} from 'react';
import { Route } from 'react-router-dom'
import UserMovies from "../UserMovies";
import AddMovie from 'components/AddMovie';
import Login from 'components/Login';
import Registration from 'components/Registration';

const styles = require('./styles.css') 

export default function Body() {

       return  (
            <div className={styles.body}>
                <Route exact path='/' render={(props) => <UserMovies {...props} userId={1} />} />
                <Route path='/add-movie' component={AddMovie} />
                <Route path='/registration' component={Registration} />
                <Route path='/login' component={Login} />
            </div>
        );
}
