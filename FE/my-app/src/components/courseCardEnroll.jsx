import React, { useEffect, useState } from "react";
import "../styles/course_card_enroll.css";
import { SlOptionsVertical, SlPlus } from "react-icons/sl";
import { IoIosClose, IoIosStar } from "react-icons/io";
import { RiShareForwardFill, RiMenuAddFill } from "react-icons/ri";
import { MdOutlineRemoveCircleOutline } from "react-icons/md";
import { useNavigate } from "react-router-dom";

import { setListInteraction } from "../redux/learning.slice";
import { useDispatch, useSelector } from "react-redux";
import axios from "axios";
import { getBaseLoad } from "../redux/baseLoader.slice";

const CourseCardEnroll = (props) => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const baseLoad = useSelector(getBaseLoad);

  const [percent, setPercent] = useState(0);
  const [rating, setRating] = useState(0);
  const coursePercent = async () => {
    try {
      const _res = await axios.get(
        `http://localhost:8084/api/user-progress/${baseLoad.user.userID}/${props.course.id}`
      );
      if (_res && _res.data) {
        let _count = _res.data.filter((item) => item.status != "NONE").length;
        setPercent(((_count / props.course.totalLectures) * 100).toFixed());
      }
    } catch (error) {}
  };
  const getCourseRatingByUser = async () => {
    try {
      const rating = await axios.get("http://localhost:8085/api/rating", {
        params: {
          user: baseLoad.user.userID,
          course: props.course.id,
        },
      });
      rating && rating.data && setRating(rating.data[0].rating);
    } catch (error) {}
  };
  useEffect(() => {
    coursePercent();
    getCourseRatingByUser();
  }, []);
  const courseClick = () => {
    navigate(`/course/${props.course.id}`);
  };
  const optShareClick = (e) => {
    e.stopPropagation();
    let tmp = props.course.thumbnails === null;
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
  const removeCourseClick = (e, courseId) => {
    e.stopPropagation();
    dispatch(
      setListInteraction({
        status: "delete-course-from-list",
        listId: props.listId,
        courseId: courseId,
      })
    );
  };
  const cardEditRatingClicked = (e, course) => {
    e.stopPropagation();
    alert(course);
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
              onClick={(e) => removeCourseClick(e, props.course.id)}
            >
              <MdOutlineRemoveCircleOutline />
              <span>Remove course</span>
            </div>
          )}
        </div>
      </div>
      <div className="course-thumnail">
        {props.course && typeof props.course.thumbnails !== Object && (
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
      <div
        className="course-rating"
        style={{ display: "flex", justifyContent: "end", color: "orange" }}
      >
        <span
          className="card-btn-edit-rating"
          onClick={(e) => cardEditRatingClicked(e, props.course.id)}
        >
          Edit
        </span>
        <div style={rating < 1 ? { color: "grey" } : {}}>
          <IoIosStar />
        </div>
        <div style={rating < 2 ? { color: "grey" } : {}}>
          <IoIosStar />
        </div>
        <div style={rating < 3 ? { color: "grey" } : {}}>
          <IoIosStar />
        </div>
        <div style={rating < 4 ? { color: "grey" } : {}}>
          <IoIosStar />
        </div>
        <div style={rating < 5 ? { color: "grey" } : {}}>
          <IoIosStar />
        </div>
      </div>
    </div>
  );
};

export default CourseCardEnroll;
