import React, { useEffect, useState } from "react";
import "../styles/login.css";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { jwtDecode } from "jwt-decode";
import { getBaseLoad, setBaseLoad } from "../redux/baseLoader.slice";
import { useDispatch, useSelector } from "react-redux";
import { toast } from "react-toastify";

const Login = () => {
  const navigate = useNavigate();

  const disatch = useDispatch();
  const baseLoad = useSelector(getBaseLoad);

  const [loginForm, setLoginForm] = useState({ username: "", password: "" });
  const [token, setToken] = useState();

  const signupClick = () => {
    navigate("/sign-up");
  };
  const formOnChange = (type, value) => {
    type == "username"
      ? setLoginForm({ ...loginForm, username: value })
      : setLoginForm({ ...loginForm, password: value });
  };
  useEffect(() => {
    setUserBaseload();
  }, [token]);
  const setUserBaseload = async () => {
    if (token && token.token != "") {
      let _jwt = jwtDecode(token.token);
      try {
        let reqBody = { username: _jwt.sub };
        const res = await axios
          .post("http://localhost:8082/api/user/findUsername", reqBody)
          .then((res) => ({
            user: {
              userID: res.data.id,
              role: res.data.role.name,
              username: res.data.username,
            },
            profile: res.data.profile,
          }));
        let time = new Date();
        time.setTime(time.getTime() + token.expires);
        localStorage.setItem(
          "_uid",
          JSON.stringify({
            token: token.token,
            expiresAt: time.toISOString(),
          })
        );
        disatch(
          setBaseLoad({ ...baseLoad, user: res.user, profile: res.profile })
        );
        window.sessionStorage.setItem("_uid", res.user.userID);
        window.sessionStorage.setItem("_role", res.user.role);
        window.sessionStorage.setItem("_uname", res.user.username);
        navigate("/");
      } catch (error) {}
    }
  };
  const login = async (e) => {
    e.preventDefault();
    let res = null;
    try {
      res = await axios
        .post("http://localhost:8082/public/auth/login", loginForm)
        .then((res) => {
          setToken({
            token: res.data.token,
            expires: res.data.expires,
          });
        });
    } catch (error) {
      toast.error("Login failed! ");
    }
  };
  return (
    <div className="login-page">
      <div className="login-container">
        <div className="login-text">
          <span>Login</span>
        </div>
        <div
          style={{ width: "250px", height: "1px", backgroundColor: "grey" }}
        ></div>
        <div className="login-form">
          <form>
            <div className="username">
              <input
                type="text"
                placeholder="username"
                required
                value={loginForm.username}
                onChange={(e) => formOnChange("username", e.target.value)}
              />
            </div>
            <div
              className="password"
              required
              value={loginForm.password}
              onChange={(e) => formOnChange("password", e.target.value)}
            >
              <input type="password" placeholder="password" autoComplete="on" />
            </div>
            <button className="btn-login" onClick={(e) => login(e)}>
              Sign in
            </button>
          </form>
        </div>
        <span style={{ margin: "5px 0", fontSize: "0.7rem" }}>
          or{" "}
          <span className="signup" onClick={() => signupClick()}>
            Sign up
          </span>
        </span>
      </div>
    </div>
  );
};

export default Login;
