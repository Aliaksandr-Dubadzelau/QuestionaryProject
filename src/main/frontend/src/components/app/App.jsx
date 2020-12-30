import React from 'react';
import Styles from './App.module.css'
import Header from '../header/Header';
import Navbar from '../navbar/Navbar';

const App = () => {
    return(
        <div className={Styles.SOMETHING}>
            <Header />
            <Navbar />
            <div className={Styles.SOMETHING}>

            </div>
        </div>
    )
}

export default App