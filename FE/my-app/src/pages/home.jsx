import React from "react";
import "../styles/home.css";
import { useSelector } from "react-redux";
import { getListAllCourses } from "../redux/coursesLoader.slice";
import CourseCard from "../components/courseCard";

const HomePage = () => {
  const courses = useSelector(getListAllCourses);

  return (
    <div className="home-page">
      <div className="billboard">
        <div className="billboard-text">
          <span className="title">Come learn with us!</span>
          <br />
          <span className="subtitle">
            Constantly cultivate knowledge and change lives - including your own
          </span>
        </div>
      </div>
      <div
        className="h-line"
        style={{
          width: "60%",
          height: "1px",
          backgroundColor: "gray",
          marginBottom: "25px",
        }}
      ></div>
      <div className="popular-courses">
        <div className="popular-courses-title">Popular</div>
        <div className="popular-courses-group">
          {courses.length > 0 &&
            courses.map((course) => <CourseCard course={course} />)}
        </div>

        <div className="featured-free-courses">
          <div className="featured-free-courses-title">
            Featured free courses
          </div>
          <div className="featured-free-courses-group"></div>
        </div>
      </div>
    </div>
  );
};

export default HomePage;
