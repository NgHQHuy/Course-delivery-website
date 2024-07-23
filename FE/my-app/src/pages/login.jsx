import React from "react";
import "../styles/login.css"
import { useNavigate } from "react-router-dom";

const Login = () => {
    const navigate = useNavigate();

    const signupClick =()=>{
        navigate("/sign-up")
    }

    return (
        <div className="login-page">
            <div className="login-container">
                <div className="login-text">
                    <span>Login</span>
                </div>
                <div style={{width: "250px", height: "1px", backgroundColor: "grey"}}></div>
                <div className="login-form">
                    <form>
                        <div className="username">
                            <input type="text" placeholder="username"/>
                        </div>
                        <div className="password">
                            <input type="text" placeholder="password"/>
                        </div>
                        <button type="submit" className="btn-login">Sign in</button>
                    </form>
                </div>
                <span style={{margin: "5px 0", fontSize: "0.7rem"}}>or <span className="signup" onClick={()=>signupClick()}>Sign up</span></span>
            </div>
        </div>
    )
}

export default Login;