import React from "react";
import "../styles/sign_up.css"
import { useNavigate } from "react-router-dom";

const Signup =()=> {
    const navigate = useNavigate()
    const loginClick =()=> {
        navigate("/login");
    }

    return (
        <div className="signup-page">
            <div className="signup-container">
                <div className="signup-text">
                    <span>Sign up</span>
                </div>
                <div style={{width: "250px", height: "1px", backgroundColor: "grey"}}></div>
                <div className="signup-form">
                    <form>
                        <div className="username">
                            <input type="text" placeholder="username"/>
                        </div>
                        <div className="password">
                            <input type="text" placeholder="password"/>
                        </div>
                        <div className="email">
                            <input type="text" placeholder="email"/>
                        </div>
                        <button type="submit" className="btn-signup">Sign in</button>
                    </form>
                </div>
                <span style={{margin: "5px 0", fontSize: "0.7rem"}}>Already have an account? <span className="login" onClick={()=>loginClick()}>Login</span></span>
            </div>
        </div>
    )
}

export default Signup