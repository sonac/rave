import * as React from 'react';
import { Switch, Route } from 'react-router-dom'
import UserMovies from "../UserMovies";
import AddMovie from 'components/AddMovie';
import Login from 'components/Login';

const styles = require('./styles.css') 

export default function Body() {

       return  (
            <div className={styles.body}>
                <Switch>
                    <Route exact path='/' render={(props) => <UserMovies {...props} userId={1} />} />
                    <Route exact path='/add-movie' component={AddMovie} />
                    <Route exact path='/login' component={Login} />
                </Switch>
            </div>
        );
}
