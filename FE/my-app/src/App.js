import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";

import "./App.css";
import "react-toastify/dist/ReactToastify.css";
import { ToastContainer, toast } from "react-toastify";
import Navbar from "./components/navbar";
import CourseInfo from "./pages/courseInfo";
import HomePage from "./pages/home";
import Learning from "./pages/learning";
import CourseContent from "./pages/courseContent";
import Cart from "./pages/cart";
import Login from "./pages/login";
import Signup from "./pages/signup";
import ManagerTools from "./pages/managerTools";
import { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { getBaseLoad, setBaseLoad } from "./redux/baseLoader.slice";
import axios from "axios";
import { jwtDecode } from "jwt-decode";

function App() {
  const dispatch = useDispatch();
  const baseLoad = useSelector(getBaseLoad);
  const fetchUser = async (req) => {
    try {
      const res = await axios
        .post("http://localhost:8082/api/user/findUsername", req)
        .then((res) => {
          let _user = {
            userID: res.data.id,
            role: res.data.role.name,
            username: res.data.username,
          };
          let _profile = res.data.profile;
          dispatch(
            setBaseLoad({ ...baseLoad, user: _user, profile: _profile })
          );
        });
    } catch (error) {}
  };
  const fetchLearning = async () => {
    let _courses = null;
    let _lists = null;
    try {
      const coursesRes = await axios
        .get(`http://localhost:8084/api/user-course/${baseLoad.user.userID}`)
        .then((res) => {
          if (res.status == 200 && res.data) {
            _courses = res.data.map((item) => item.id);
          }
        });
      const listsRes = await axios
        .get(`http://localhost:8084/api/user-list/${baseLoad.user.userID}`)
        .then((res) => {
          if (res.status == 200 && res.data) {
            _lists = res.data.map((item) => item.id);
          }
        });
      _courses != null && _lists != null
        ? dispatch(
            setBaseLoad({
              ...baseLoad,
              learning: { courses: _courses, lists: _lists },
            })
          )
        : toast.warn("Something wrong!");
    } catch (error) {}
  };
  const fetchCart = async () => {
    let _cart = null;
    try {
      const res = await axios
        .get(`http://localhost:8083/api/cart/${baseLoad.user.userID}`)
        .then((res) => {
          if (res.status == 200 && res.data) {
            _cart = res.data;
          } else {
            console.log("Cart not found!");
          }
        });
      _cart != null
        ? dispatch(
            setBaseLoad({
              ...baseLoad,
              cart: { cartID: null, cartItems: _cart },
            })
          )
        : console.log("Not found");
    } catch (error) {}
  };
  useEffect(() => {
    if (baseLoad.user.userID && baseLoad.user.userID != null) {
      fetchLearning();
      fetchCart();
    } else if (localStorage.getItem("_uid")) {
      let _token = localStorage.getItem("_uid");
      let _jwt = jwtDecode(_token.split(",")[0].split(":")[1]);
      fetchUser({ username: _jwt.sub });
    }
  }, [baseLoad.user.userID]);
  return (
    <BrowserRouter>
      <div className="App">
        <header className="App-header">
          <Navbar></Navbar>
        </header>
        <div className="App-body">
          <Routes>
            <Route index element={<HomePage />} />
            <Route path="/*" element={<Navigate to={"/"} replace={true} />} />
            <Route path="/" element={<HomePage />} />
            <Route path="courses/*" element={<CourseInfo />} />
            <Route path="course/*" element={<CourseContent />} />
            <Route path="learning" element={<Learning />} />
            <Route path="cart/*" element={<Cart />} />
            <Route path="login" element={<Login />} />
            <Route path="sign-up" element={<Signup />} />
            <Route path="manager-tools" element={<ManagerTools />} />
          </Routes>
        </div>
        <footer className="App-footer"></footer>
      </div>
      <ToastContainer autoClose={1200} style={{ fontWeight: "600" }} />
    </BrowserRouter>
  );
}

export default App;
