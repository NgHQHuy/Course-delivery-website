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
import {
  getListAllCourses,
  setListAllCourses,
} from "./redux/coursesLoader.slice";

function App() {
  const dispatch = useDispatch();
  const baseLoad = useSelector(getBaseLoad);
  const sysCourses = useSelector(getListAllCourses);
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

  // const fetchCart = async () => {
  //   let _cart = null;
  //   try {
  //     const res = await axios
  //       .get(`http://localhost:8083/api/cart/${baseLoad.user.userID}`)
  //       .then((res) => {
  //         if (res.status == 200 && res.data) {
  //           _cart = res.data;
  //         } else {
  //           console.log("Cart not found!");
  //         }
  //       });
  //     _cart != null
  //       ? dispatch(
  //           setBaseLoad({
  //             ...baseLoad,
  //             cart: { cartID: null, cartItems: _cart },
  //           })
  //         )
  //       : console.log("Not found");
  //   } catch (error) {}
  // };
  const fetchSysCourses = async () => {
    let _sysCourses = [];
    try {
      const res = await axios.get("http://localhost:8081/api/course");
      if (res.data) {
        for (let i of res.data) {
          try {
            const _instructor = await axios.get(
              `http://localhost:8081/api/instructor/${i.instructorId}`
            );
            if (_instructor) {
              i = { ...i, instructorName: _instructor.data.name };
            }
          } catch (error) {
            i = { ...i, instructorName: "Unknown" };
          }
          try {
            const _avgRating = await axios.get(
              "http://localhost:8085/api/rating/getAverage",
              {
                params: { course: i.id },
              }
            );
            if (_avgRating) {
              i = { ...i, rating: _avgRating.data.average };
            }
          } catch (error) {
            i = { ...i, rating: 0 };
          }
          _sysCourses = [..._sysCourses, i];
        }
        dispatch(setListAllCourses(_sysCourses));
      }
    } catch (error) {}
  };
  useEffect(() => {
    if (baseLoad.user.userID && baseLoad.user.userID != "") {
    } else if (localStorage.getItem("_uid")) {
      let _token = localStorage.getItem("_uid");
      let _jwt = jwtDecode(_token.split(",")[0].split(":")[1]);
      fetchUser({ username: _jwt.sub });
    }
    fetchSysCourses();
  }, [baseLoad.user]);
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
