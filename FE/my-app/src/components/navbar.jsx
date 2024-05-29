import React from "react";
import "../styles/navbar.css"
import { HiOutlineQueueList } from "react-icons/hi2";
import { BsSearch, BsCart3 } from "react-icons/bs";
import { Link } from "react-router-dom";

const Navbar = () => {
    return (
        <nav>
            <div className="logo"> 
                <a href="/"><img src="https://www.udemy.com/staticx/udemy/images/v7/logo-udemy.svg" alt="Udemy" width="91" height="34" loading="lazy"/></a>  
            </div>
            <div className="search">
                <div className="search-group">
                    <div className="search-icon"><BsSearch /></div>
                    <input type="text" placeholder="Search for anything..." />
                </div>
            </div>
            <div className="my-learning">
                <span>My learning</span>
            </div>
            <div className="cart">
                <div className="cart-icon"><BsCart3 size={24}/></div>
            </div>
            <div className="user">
                <div className="user-avatar"></div>
            </div>

            <div className="menu">
                <div className="menu-icon"><HiOutlineQueueList size={24}/></div>
            </div>
        </nav>
    );
}

export default Navbar;