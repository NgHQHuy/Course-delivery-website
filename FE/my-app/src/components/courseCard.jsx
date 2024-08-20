import axios from "axios";
import React from "react";
import { useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import { getBaseLoad } from "../redux/baseLoader.slice";

const CourseCard = (props) => {
  const navigate = useNavigate();
  const baseLoad = useSelector(getBaseLoad);

  const handleCourseClicked = async (title) => {
    try {
      const checkRes = await axios.post(
        "http://localhost:8084/api/user-course/checkRegistered",
        { courseId: props.course.id, userId: baseLoad.user.userID }
      );
      if (checkRes && checkRes.data) {
        navigate(`course/${props.course.id}`);
      } else {
        navigate(`/courses/${title}`, { state: props.course.id });
      }
    } catch (error) {}
  };
  return (
    <div
      className="card-course"
      key={props.course.id}
      onClick={() => handleCourseClicked(props.course.title)}
    >
      <div className="card-course-thumnail"></div>
      <div className="card-course-title">{props.course.title}</div>
      <div className="card-course-author">{props.course.instructorName}</div>
      <div className="card-course-rating">{props.course.rating}</div>
      <div className="card-course-price">
        {props.course.price.toLocaleString("it-IT", {
          style: "currency",
          currency: "VND",
        })}
      </div>
    </div>
  );
};

export default CourseCard;
