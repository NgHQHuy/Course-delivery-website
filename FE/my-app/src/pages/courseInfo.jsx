import React, { useEffect, useState } from "react";
import "../styles/course_info.css";
import { FaChevronRight, FaChevronDown } from "react-icons/fa";
import d_avatar from "../assets/default_avatar.jpg";

import { useLocation, useNavigate } from "react-router-dom";
import { useSelector } from "react-redux";
import { getListAllCourses } from "../redux/coursesLoader.slice";
import axios from "axios";
import { getBaseLoad } from "../redux/baseLoader.slice";
import { toast } from "react-toastify";

const CourseInfo = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const baseLoad = useSelector(getBaseLoad);
  const courses = useSelector(getListAllCourses);
  const [overview, setOverview] = useState(
    courses.find((item) => item.id == location.state)
  );
  const [courseSyllabus, setCourseSyllabus] = useState();
  const [sectionSelection, setSectionSelection] = useState([]);
  const [instructor, setInstructor] = useState();
  const fetchData = async () => {
    try {
      const res = await axios.get(
        `http://localhost:8081/api/course/${location.state}/overview`
      );
      setOverview({
        ...overview,
        totalLectures: res.data.toltalLectures,
        numOfStudents: res.data.numOfStudent,
        length: res.data["length"],
      });

      const _syllabusRes = await axios.get(
        `http://localhost:8081/api/course/${location.state}/syllabus`
      );
      let _syllabus = _syllabusRes.data.map((item) => ({
        ...item,
        lectures: [...item.lectures.sort((a, b) => a.position - b.position)],
      }));
      _syllabus = _syllabus.sort((a, b) => a.position - b.position);
      setCourseSyllabus(_syllabus);

      const _instuctor = await axios.get(
        `http://localhost:8081/api/instructor/${overview.instructorId}`
      );
      setInstructor(_instuctor.data);
    } catch (error) {}
  };
  useEffect(() => {
    fetchData();
  }, []);

  const addToCartClicked = async () => {
    if (baseLoad.user.userID === null) navigate("/login");
    else {
      if (baseLoad.user.role == "ADMIN" || "MANAGER") {
        toast.warn("Your role has no access");
      } else {
        try {
          const res = await axios.post("http://localhost:8083/api/cart/add", {
            userId: baseLoad.user.userID,
            courseId: overview.id,
          });
        } catch (error) {
          toast.warn("Add failed, try again!");
        }
      }
    }
  };
  const buyNowClicked = () => {
    if (baseLoad.user.userID === null) navigate("/login");
    else {
      if (baseLoad.user.role == "ADMIN" || "MANAGER") {
        toast.warn("Your role has no access");
      } else {
        try {
        } catch (error) {
          toast.warn("Add failed, try again!");
        }
      }
    }
  };
  return (
    overview && (
      <div className="course-info">
        <div className="course-info-main">
          <div className="introduce">
            <div className="title">{overview.title}</div>
            <div className="summary">{overview.description}</div>
            <div className="rating">{overview.rating}</div>
            <div className="author">
              Created by{" "}
              <span
                style={{
                  fontStyle: "italic",
                  textDecoration: "underline",
                  textUnderlineOffset: "4px",
                }}
              >
                {overview.instructorName}
              </span>
            </div>
            <div className="create-time" style={{ marginBottom: "5px" }}>
              Created at{" "}
              {new Intl.DateTimeFormat(["ban", "id"]).format(
                new Date(overview.createdAt)
              )}
            </div>
            <div className="update-time">
              Last updated{" "}
              {new Intl.DateTimeFormat(["ban", "id"]).format(
                new Date(overview.updatedAt)
              )}
            </div>
          </div>
          <div className="content">
            <div className="content-header">Course content</div>
            <div className="content-container">
              {courseSyllabus &&
                courseSyllabus.map((item) => (
                  <div className="_content-section" key={item.id}>
                    <div
                      className="_content-section-title"
                      onClick={() => {
                        sectionSelection.find((section) => section == item.id)
                          ? setSectionSelection(
                              [...sectionSelection].filter(
                                (section) => section != item.id
                              )
                            )
                          : setSectionSelection([...sectionSelection, item.id]);
                      }}
                    >
                      {sectionSelection.find(
                        (section) => section == item.id
                      ) ? (
                        <FaChevronDown size={12} />
                      ) : (
                        <FaChevronRight size={12} />
                      )}

                      <span>{item.title}</span>
                      <span
                        style={{
                          position: "absolute",
                          right: "5px",
                          fontWeight: "lighter",
                        }}
                      >
                        {item.lectures.length + " lectures"}
                      </span>
                    </div>
                    <div
                      className="_content-lectures-group"
                      style={
                        sectionSelection.find((section) => section == item.id)
                          ? {}
                          : { display: "none" }
                      }
                    >
                      {item.lectures.length > 0 &&
                        item.lectures.map((lec) => (
                          <div key={lec.id}>{lec.title}</div>
                        ))}
                    </div>
                  </div>
                ))}
            </div>
          </div>
          <div className="description">
            <div className="description-header">Description</div>
            <div style={{ textAlign: "left", padding: "10px 20px" }}>
              {overview.summary}
            </div>
          </div>
          <div className="requirements">
            <div className="requirements-header">Requirements</div>
            <div>{overview.requirement}</div>
          </div>
          <div className="instructor">
            <div className="instructor-header">Instructor</div>
            <div style={{ display: "flex", height: "50px", margin: "10px 0" }}>
              <div
                style={{
                  width: "50px",
                  height: "50px",
                  display: "block",
                  borderRadius: "50%",
                  margin: "0 10px",
                }}
              >
                <img
                  src={
                    instructor &&
                    instructor.avatar &&
                    instructor.avatar.split("/")[0] == "data:image"
                      ? instructor.avatar
                      : d_avatar
                  }
                  alt="avatar"
                  style={{
                    height: "100%",
                    width: "100%",
                    objectFit: "cover",
                    borderRadius: "50%",
                  }}
                  onClick={() => console.log(instructor)}
                />
              </div>
              <span style={{ lineHeight: "50px" }}>
                {overview.instructorName}
              </span>
            </div>
          </div>
          <div className="more-by-instructor">
            <div className="more-header">More courses by ABC</div>
          </div>
        </div>
        <div className="course-info-bar">
          <div className="course-info-card">
            <div className="card-preview">
              <img
                src={overview && overview.thumbnail ? overview.thumbnail : ""}
                alt="course preview image"
                style={{ width: "100%", height: "100%", objectFit: "cover" }}
              />
            </div>
            <div className="card-content">
              <div className="price">
                {overview.price.toLocaleString("it-IT", {
                  style: "currency",
                  currency: "VND",
                })}
              </div>
              <div className="button-group">
                <div className="button-group-layer1">
                  <div className="add-to-cart">
                    <button type="button" onClick={() => addToCartClicked()}>
                      Add to cart
                    </button>
                  </div>
                </div>
                <div className="button-group-layer2">
                  <button type="button" onClick={() => buyNowClicked()}>
                    Buy now
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    )
  );
};

export default CourseInfo;
