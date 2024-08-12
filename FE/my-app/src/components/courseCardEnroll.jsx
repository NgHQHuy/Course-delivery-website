import React from "react";
import "../styles/course_card_enroll.css";
import { SlOptionsVertical, SlPlus } from "react-icons/sl";
import { RiShareForwardFill, RiMenuAddFill } from "react-icons/ri";
import { MdOutlineRemoveCircleOutline } from "react-icons/md";
import { useNavigate } from "react-router-dom";

import { setListInteraction } from "../redux/learning.slice";
import { useDispatch } from "react-redux";

const CourseCardEnroll = (props) => {
  const navigate = useNavigate();
  const dispatch = useDispatch();

  const percent = 75;

  const courseClick = () => {
    navigate(`/course/${props.course.id}`);
  };
  const optShareClick = (e) => {
    e.stopPropagation();
    let tmp = props.course.thumbnails === null;
    console.log(tmp);
  };
  const optCreateListClick = (e) => {
    e.stopPropagation();
    dispatch(
      setListInteraction({ status: "create", courseID: props.course.id })
    );
  };
  const optAddToListClick = (e) => {
    e.stopPropagation();
    dispatch(
      setListInteraction({ status: "add-to-list", courseId: props.course.id })
    );
  };
  const removeCourseClick = (e) => {
    e.stopPropagation();
    alert("remove");
  };

  return (
    <div
      className="course-card"
      onClick={() => courseClick()}
      key={props.course.id}
    >
      <div className="course-options">
        <SlOptionsVertical size={16} className="options-icon" />
        <div className="options-dropdown">
          {props.pageView === "all-courses" ? (
            <>
              <div className="share" onClick={(e) => optShareClick(e)}>
                <RiShareForwardFill />
                <span>Share</span>
              </div>
              <div
                className="create-new-list"
                onClick={(e) => optCreateListClick(e)}
              >
                <SlPlus />
                <span>Create new list</span>
              </div>
              <div
                className="add-to-list"
                onClick={(e) => optAddToListClick(e)}
              >
                <RiMenuAddFill />
                <span>Add to existing list</span>
              </div>
            </>
          ) : (
            <div
              className="remove-course-from-list"
              onClick={(e) => removeCourseClick(e)}
            >
              <MdOutlineRemoveCircleOutline />
              <span>Remove course</span>
            </div>
          )}
        </div>
      </div>
      <div className="course-thumnail">
        {props.course.thumbnails !== null && (
          // props.course.thumbnails.split("/")[0] == "data:image" && (
          <img src={props.course.thumbnails} />
        )}
      </div>
      <div className="course-title">{props.course.title}</div>
      <div className="course-author">{props.course.instructorName}</div>
      <div className="course-progression">
        <div className="course-progression-bar">
          <div className="bar-percent" style={{ width: `${percent}%` }}></div>
        </div>
        <div className="course-progression-text">{percent}%</div>
      </div>
    </div>
  );
};

export default CourseCardEnroll;
