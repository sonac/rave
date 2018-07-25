import * as React from 'react';
import { Component } from 'react';

// 'HelloProps' describes the shape of props.
// State is never set so we use the '{}' type.

interface Actions {}
  
interface Props {}
  
type State = {}

export class App extends Component<Props, State> {
    render() {
        return <h1>Sholom, world!</h1>;
    }
}