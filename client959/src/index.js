import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './app/App.js';
import * as serviceWorker from './serviceWorker';
import { BrowserRouter as Router } from 'react-router-dom'; //import rounter

ReactDOM.render(
    <Router>
    <App />, 
    </Router>,
    document.getElementById('root'));

serviceWorker.register();
