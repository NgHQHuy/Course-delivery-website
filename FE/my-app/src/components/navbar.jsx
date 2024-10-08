import React, { useEffect, useState } from "react";
import "../styles/navbar.css";
import { HiOutlineQueueList } from "react-icons/hi2";
import { BsSearch, BsCart3 } from "react-icons/bs";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import d_avatar from "../assets/default_avatar.jpg";

import { getBaseLoad, cleanBaseLoad } from "../redux/baseLoader.slice";
import { useDispatch, useSelector } from "react-redux";

const Navbar = () => {
  const base_loader = useSelector(getBaseLoad);
  const dispatch = useDispatch();

  const navigate = useNavigate();

  const [logged, setLogged] = useState(false);

  useEffect(() => {
    base_loader.user.userID ? setLogged(true) : setLogged(false);
  }, [base_loader]);

  const learningClick = () => {
    navigate(`/learning`);
  };
  const openCart = () => {
    if (logged) {
      navigate("/cart");
    } else {
      toast.warn("Sign-in or Sign-up first!");
    }
  };
  const logout = () => {
    dispatch(cleanBaseLoad());
    localStorage.removeItem("_uid");
    window.sessionStorage.removeItem("_uid");
    window.sessionStorage.removeItem("_role");
    window.sessionStorage.removeItem("_uname");
    navigate("/");
  };

  return (
    <nav>
      <div className="logo">
        <a href="/">
          <img
            src="https://www.udemy.com/staticx/udemy/images/v7/logo-udemy.svg"
            alt="Udemy"
            width="91"
            height="34"
            loading="lazy"
          />
        </a>
      </div>
      <div className="search">
        <div className="search-group">
          <div className="search-icon">
            <BsSearch />
          </div>
          <input type="text" placeholder="Search" />
        </div>
      </div>
      <div
        className="my-learning"
        style={
          !logged || base_loader.user.role != "USER" ? { display: "none" } : {}
        }
      >
        <span onClick={() => learningClick()}>My learning</span>
      </div>
      <div
        className="cart"
        onClick={() => openCart()}
        style={base_loader.user.role == "USER" ? {} : { display: "none" }}
      >
        <div className="cart-icon">
          <BsCart3 size={24} />
        </div>
      </div>
      <div
        className="nav-manager-tools"
        style={
          base_loader.user.role == "ADMIN" || base_loader.user.role == "MANAGER"
            ? { cursor: "pointer" }
            : { display: "none" }
        }
        onClick={() => navigate("/manager-tools")}
      >
        Manager tools
      </div>
      {logged ? (
        <div className="user">
          <div className="user-avatar" onClick={() => navigate("/profile")}>
            <img
              src={
                base_loader.profile && base_loader.profile.avatar
                  ? base_loader.profile.avatar
                  : d_avatar
              }
              alt="nav-user-avatar"
              style={{
                width: "30px",
                height: "30px",
                objectFit: "cover",
                borderRadius: "50%",
              }}
            />
          </div>
          <div className="dropdown-menu">
            <ul className="profile-n-account">
              <li onClick={() => navigate("/account#edit")}>
                Account settings
              </li>
            </ul>
            <ul className="logout">
              <li onClick={() => logout()}>Log out</li>
            </ul>
          </div>
        </div>
      ) : (
        <>
          <div className="nav-signin" onClick={() => navigate("/login")}>
            Sign in
          </div>
          <div className="nav-signup" onClick={() => navigate("/sign-up")}>
            Sign up
          </div>
        </>
      )}

      <div className="search-responsive">
        <div className="search-icon-respon">
          <BsSearch />
        </div>
      </div>
      <div className="menu">
        <div className="menu-icon">
          <HiOutlineQueueList size={24} />
        </div>
        <div className="dropdown-menu">
          {logged ? (
            <>
              <ul className="profile-n-account">
                <li onClick={() => navigate("/profile")}>Edit profile</li>
                <li onClick={() => navigate("account#edit")}>
                  Account settings
                </li>
              </ul>
              <ul>
                <li
                  style={
                    base_loader.user.role == "ADMIN" ||
                    base_loader.user.role == "MANAGER"
                      ? {}
                      : { display: "none" }
                  }
                  onClick={() => navigate("/manager-tools")}
                >
                  Manager tools
                </li>
              </ul>
              <ul
                className="learning-n-cart"
                style={
                  base_loader.user.role == "USER" ? {} : { display: "none" }
                }
              >
                <li onClick={() => learningClick()}>My learning</li>
                <li onClick={() => openCart()}>Cart</li>
              </ul>
              <ul className="logout">
                <li onClick={() => logout()}>Log out</li>
              </ul>
            </>
          ) : (
            <>
              <ul className="signin-n-signup">
                <li
                  style={{ borderBottom: "1px solid rgb(200, 200, 200)" }}
                  onClick={() => navigate("/login")}
                >
                  Sign in
                </li>
                <li onClick={() => navigate("/sign-up")}>Sign up</li>
              </ul>
            </>
          )}
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
