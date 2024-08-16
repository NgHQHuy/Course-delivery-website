import React from "react";
import { useNavigate } from "react-router-dom";

const CourseCard = (props) => {
  const navigate = useNavigate();

  const handleCourseClicked = (title) => {
    navigate(`/courses/${title}`, { state: props.course.id });
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
