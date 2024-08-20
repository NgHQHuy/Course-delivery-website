import React, { useEffect, useState } from "react";
import "../styles/course_content.css";
import { FaAnglesLeft, FaAngleDown } from "react-icons/fa6";
import { FaCheck } from "react-icons/fa";
import { IoIosClose, IoIosStar, IoIosAlert } from "react-icons/io";
import { IoDocumentTextOutline } from "react-icons/io5";
import { GoVideo } from "react-icons/go";

import axios from "axios";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import { useSelector } from "react-redux";
import { getBaseLoad } from "../redux/baseLoader.slice";

const CourseContent = () => {
  const navigate = useNavigate();
  const baseLoad = useSelector(getBaseLoad);

  const [menuContent, setMenuContent] = useState("");
  const [sectionShow, setSectionShow] = useState([]);
  const [tabSelected, setTabSelected] = useState("overview");

  const _courseId = window.location.href.split("/")[4];
  const [overview, setOverview] = useState({});
  const [syllabus, setSyllabus] = useState([]);
  const [progress, setProgress] = useState();
  const [currentLecture, setCurrentLecture] = useState("");
  const [lectureContent, setLectureContent] = useState("");

  const getData = async () => {
    let _syllabus = [];
    let _progress = [];
    let _currentLecture = {};
    try {
      const _overviewRes = await axios.get(
        `http://localhost:8081/api/course/${_courseId}/overview`
      );
      _overviewRes && _overviewRes.data && setOverview(_overviewRes.data);
    } catch (error) {
      toast.warn("Course overview load failed!");
    }
    try {
      const _syllabusRes = await axios.get(
        `http://localhost:8081/api/course/${_courseId}/syllabus`
      );
      if (_syllabusRes && _syllabusRes.data) {
        _syllabus = _syllabusRes.data.map((item) => ({
          ...item,
          lectures: [...item.lectures.sort((a, b) => a.position - b.position)],
        }));
        _syllabus = _syllabus.sort((a, b) => a.position - b.position);
      }
      let _tmpSyllabus = _syllabus;
      for (let item of _tmpSyllabus) {
        let _tmpLecId = item.lectures.map((lec) => lec.id);
        let _tmpLecDetails = [];
        for (let i of _tmpLecId) {
          const _res = await axios.get(
            `http://localhost:8081/api/course/${_courseId}/${item.id}/${i}`
          );
          _tmpLecDetails = [..._tmpLecDetails, _res.data];
        }
        _syllabus = _syllabus.map((sec) =>
          sec.id == item.id ? { ...sec, lectures: _tmpLecDetails } : sec
        );
      }
      console.log("syllabus", _syllabus);
      _currentLecture = {
        section: _syllabus.find((item) => item.position == 1).id,
        lecture: _syllabus
          .find((item) => item.position == 1)
          .lectures.find((lec) => lec.position == 1).id,
        type: _syllabus
          .find((item) => item.position == 1)
          .lectures.find((lec) => lec.position == 1).type,
        value: _syllabus
          .find((item) => item.position == 1)
          .lectures.find((lec) => lec.position == 1).value,
      };
      setSyllabus(_syllabus);
    } catch (error) {}
    try {
      const _progressRes = await axios.get(
        `http://localhost:8084/api/user-progress/${baseLoad.user.userID}/${_courseId}`
      );
      if (_progressRes && _progressRes.data) {
        console.log("progress", _progressRes.data);
        _progress = _progressRes.data;
        setProgress(_progress);
        let _onGoingLec = _progressRes.data.find(
          (item) => item.status == "ON_GOING"
        );
        if (_onGoingLec) {
          _currentLecture = {
            section: _onGoingLec.sectionId,
            lecture: _onGoingLec.lectureId,
            type: _syllabus
              .find((item) => item.id == _onGoingLec.sectionId)
              .lectures.find((lec) => lec.id == _onGoingLec.lectureId).type,
            value: _syllabus
              .find((item) => item.id == _onGoingLec.sectionId)
              .lectures.find((lec) => lec.id == _onGoingLec.lectureId).value,
          };
        }
      }
    } catch (error) {}
    _currentLecture = {
      ..._currentLecture,
      timestamp:
        _currentLecture.type == "video"
          ? _progress.find((lec) => lec.lectureId == _currentLecture.lecture)
              .timestamp
          : "",
    };
    setCurrentLecture(_currentLecture);
  };

  useEffect(() => {
    getData();
  }, []);

  const setMenuStatus = () => {
    menuContent === "" ? setMenuContent("active") : setMenuContent("");
  };

  const tabSelectionClick = (tab) => {
    setTabSelected(tab);
  };

  const lectureCheckBoxClicked = async (lecture) => {
    const req = {
      userId: baseLoad.user.userID,
      courseId: _courseId,
      lectureId: lecture,
      timestamp: progress.find((item) => item.lectureId == lecture).timestamp,
      status:
        progress.find((item) => item.lectureId == lecture).status == "NONE"
          ? "DONE"
          : "NONE",
    };
    try {
      const res = await axios.post(
        "http://localhost:8084/api/user-progress",
        req
      );
      res &&
        res.status == 200 &&
        setProgress([
          ...progress.map((item) =>
            item.lectureId == lecture ? { ...item, status: req.status } : item
          ),
        ]);
    } catch (error) {}
  };

  const lectureOnClick = (section, lecture, type, value) => {
    if (currentLecture.type == "video") {
      let video = document.getElementById("video-player");
      if (video.currentTime) {
        video.currentTime != video.duration
          ? setProgress([
              ...progress.map((item) =>
                item.lectureId == currentLecture.lecture
                  ? { ...item, timestamp: video.currentTime }
                  : item
              ),
            ])
          : setProgress([
              ...progress.map((item) =>
                item.lectureId == currentLecture.lecture
                  ? { ...item, timestamp: 0 }
                  : item
              ),
            ]);
      }
      console.log("video", video);
    }
    console.log("current", currentLecture);
    if (type == "video") {
      let video = document.getElementById("video-player");
      let _timestamp = progress.find(
        (item) => item.lectureId == lecture
      ).timestamp;
      setCurrentLecture({
        section: section,
        lecture: lecture,
        type: type,
        value: value,
        timestamp: _timestamp,
      });
      video.load();
      video.play();
      // document
      //   .getElementById("video-source")
      //   .setAttribute(
      //     "src",
      //     `http://localhost:8080/videos/stream/${value}.mp4`
      //   );
      // video.load();
      // video.currentTime = _timestamp;
      // video.play();
    } else {
      if (currentLecture.type == "video") {
        let video = document.getElementById("video-player");
        video.pause();
      }

      setCurrentLecture({
        section: section,
        lecture: lecture,
        type: type,
        value: value,
        timestamp: 0,
      });
    }
  };

  return (
    <div className="course-content">
      <div className="column-display">
        {menuContent === "" ? (
          <div className="btn-menu-slide" onClick={() => setMenuStatus()}>
            <div className="icon">
              <FaAnglesLeft size={20} />
            </div>
            <div className="text">
              <span>Course content</span>
            </div>
          </div>
        ) : (
          <></>
        )}
        <div
          className="course-content-media"
          onContextMenu={(e) => {
            e.preventDefault();
          }}
        >
          <video
            id="video-player"
            autoPlay
            controls
            controlsList="nodownload"
            style={
              currentLecture && currentLecture.type != "video"
                ? { display: "none" }
                : {}
            }
          >
            <source
              id="video-source"
              src={`http://localhost:8080/videos/stream/${currentLecture.value}.mp4#t=${currentLecture.timestamp}`}
              type="video/mp4"
            />
          </video>

          <div
            style={
              currentLecture && currentLecture.type != "text"
                ? { display: "none" }
                : {
                    height: "100%",
                    backgroundColor: "white",
                    padding: "10px 20px",
                  }
            }
          >
            <div style={{ textAlign: "left" }}>{currentLecture.value}</div>
          </div>
        </div>
        <div className="course-content-other">
          <div className="tab-selection">
            {tabSelected === "overview" ? (
              <div className="overview tabActived">
                <span>Overview</span>
              </div>
            ) : (
              <div
                className="overview"
                onClick={() => tabSelectionClick("overview")}
              >
                Overview
              </div>
            )}
            {tabSelected === "reviews" ? (
              <div className="reviews tabActived">
                <span>Reviews</span>
              </div>
            ) : (
              <div
                className="reviews"
                onClick={() => tabSelectionClick("reviews")}
              >
                Reviews
              </div>
            )}
          </div>
          <div className="tab-content">
            {tabSelected === "overview" ? (
              <div className="overview-content">
                <div className="overview-content-info">
                  <div className="title">{overview && overview.title}</div>
                  <div className="by-number">
                    <div className="rating">
                      <div className="by-number-group">
                        <span>{overview && overview.rating}</span>{" "}
                        <IoIosStar size={20} />
                      </div>
                    </div>
                    <div className="num-of-students">
                      <div className="by-number-group">
                        <span>{overview && overview.numOfStudent}</span>
                      </div>
                      Students
                    </div>
                    <div className="total-hours">
                      <div className="by-number-group">
                        <span>{overview && overview.totalLectures}</span>
                      </div>
                      Total lectures
                    </div>
                  </div>
                  <div className="last-updated">
                    <IoIosAlert size={20} />{" "}
                    <span>
                      {overview && overview.updatedAt}
                      {/* {overview &&
                        new Intl.DateTimeFormat(["ban", "id"]).format(
                          new Date(overview.updatedAt)
                        )} */}
                    </span>
                  </div>
                </div>
                <div className="overview-content-description">
                  <div className="grid-grow-1-d">Description:</div>
                  <div className="grid-grow-2-d">
                    <div className="description-text">
                      <p>{overview && overview.description}</p>
                    </div>
                  </div>
                </div>
                <div className="overview-content-instructor">
                  <div className="grid-grow-1-i"></div>
                  <div className="grid-grow-2-i"></div>
                </div>
              </div>
            ) : (
              <div className="reviews-content"></div>
            )}
          </div>
        </div>
      </div>
      {menuContent === "active" ? (
        <div className="column-lectures-menu">
          <div className="menu-header">
            <span>Course content</span>
            <div className="btn-close-menu" onClick={() => setMenuStatus()}>
              <IoIosClose size={32} className="btn-close-menu-icon" />
            </div>
          </div>
          <div className="menu-bogy">
            {syllabus &&
              syllabus.map((item) => (
                <div className="section" key={item.id}>
                  <div
                    className="section-header"
                    onClick={() => {
                      sectionShow.find((sec) => sec == item.id)
                        ? setSectionShow([
                            ...sectionShow.filter((s) => s != item.id),
                          ])
                        : setSectionShow([...sectionShow, item.id]);
                    }}
                  >
                    <div className="section-header-info">
                      <span>{item.title}</span>
                      <br />
                      {
                        progress.filter(
                          (i) => i.sectionId == item.id && i.status != "NONE"
                        ).length
                      }
                      /{item.lectures.length}
                    </div>
                    {sectionShow.find((sec) => sec == item.id) ? (
                      <div className="section-header-icon">
                        <FaAngleDown size={16} />
                      </div>
                    ) : (
                      <div className="section-header-icon-rotate">
                        <FaAngleDown size={16} />
                      </div>
                    )}
                  </div>
                  {/* {sectionShow === "" ?
                                <div className="section-body"></div>
                                :
                                <div className="section-body" style={{display: "none"}}></div>
                            }  */}
                  <div
                    className="section-body"
                    style={
                      !sectionShow.find((sec) => sec == item.id)
                        ? { display: "none" }
                        : { display: "flex", flexWrap: "wrap" }
                    }
                  >
                    {item.lectures.length > 0
                      ? item.lectures.map((lecture) => (
                          <div
                            className="lecture"
                            style={
                              currentLecture.lecture != lecture.id
                                ? { width: "100%" }
                                : {
                                    width: "100%",
                                    backgroundColor: "rgb(200,200,200)",
                                  }
                            }
                            key={lecture.id}
                            onClick={() =>
                              lectureOnClick(
                                item.id,
                                lecture.id,
                                lecture.type,
                                lecture.value
                              )
                            }
                          >
                            <div className="lecture-checkbox">
                              <div
                                className="_checkbox"
                                onClick={() =>
                                  lectureCheckBoxClicked(lecture.id)
                                }
                              >
                                {progress.find(
                                  (i) =>
                                    i.lectureId == lecture.id &&
                                    i.status != "NONE"
                                ) && (
                                  <div
                                    style={{
                                      width: "15px",
                                      height: "15px",
                                      backgroundColor: "grey",
                                      color: "white",
                                      display: "flex",
                                      alignItems: "center",
                                      justifyContent: "center",
                                    }}
                                  >
                                    <FaCheck size={12} />
                                  </div>
                                )}
                              </div>
                            </div>
                            <div className="lecture-info">
                              <div className="lecture-info-title">
                                {lecture.position + ". " + lecture.title}
                              </div>
                              <div className="lecture-info-more">
                                {lecture.type == "video" ? (
                                  <GoVideo />
                                ) : (
                                  <IoDocumentTextOutline />
                                )}
                                <span>{lecture["length"]} mins</span>
                              </div>
                            </div>
                          </div>
                        ))
                      : "Empty"}
                  </div>
                </div>
              ))}
          </div>
        </div>
      ) : (
        <></>
      )}
    </div>
  );
};

export default CourseContent;
