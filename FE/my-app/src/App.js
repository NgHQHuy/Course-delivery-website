import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";

import "./App.css";
import "react-toastify/dist/ReactToastify.css";
import { ToastContainer } from "react-toastify";
import Navbar from "./components/navbar";
import CourseInfo from "./pages/courseInfo";
import HomePage from "./pages/home";
import Learning from "./pages/learning";
import CourseContent from "./pages/courseContent";
import Cart from "./pages/cart";
import Login from "./pages/login";
import Signup from "./pages/signup";
import ManagerTools from "./pages/managerTools";

function App() {
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
            <Route path="learning" element={<Learning />} />
            <Route path="course/*" element={<CourseContent />} />
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
