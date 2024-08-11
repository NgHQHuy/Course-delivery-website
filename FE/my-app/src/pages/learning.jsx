import React, { useEffect, useState } from "react";
import "../styles/learning.css";
import CourseCardEnroll from "../components/courseCardEnroll";
import { MdEdit, MdDelete } from "react-icons/md";

import {
  setCourses,
  setLists,
  setListInteraction,
  getListInteraction,
  getCourses,
  getLists,
} from "../redux/learning.slice";
import { useDispatch, useSelector } from "react-redux";
import axios, { all } from "axios";
import { getBaseLoad, setBaseLoad } from "../redux/baseLoader.slice";
import { getListAllCourses } from "../redux/coursesLoader.slice";
import { toast } from "react-toastify";

const Learning = () => {
  const dispatch = useDispatch();
  const baseLoad = useSelector(getBaseLoad);
  const allCourses = useSelector(getListAllCourses);
  const listInteraction = useSelector(getListInteraction);
  const userCourses = useSelector(getCourses);
  const userLists = useSelector(getLists);

  const [tabSelected, setTabSelected] = useState("all-courses");
  const [listForm, setListForm] = useState({
    id: null,
    title: "",
    description: "",
  });

  const fetchLearning = async () => {
    let _courses = [];
    let _lists = [];
    try {
      const coursesRes = await axios.get(
        `http://localhost:8084/api/user-course/${baseLoad.user.userID}`
      );
      for (let i of coursesRes.data) {
        let _course = await axios.get(
          `http://localhost:8081/api/course/${i.courseId}/overview`
        );
        let { totalLectures, numOfStudent, length } = _course.data;
        allCourses.map((i) =>
          typeof i.thumbnails === Object ? (i.thumbnails = "") : i
        );
        _courses = [
          ..._courses,
          {
            ...allCourses.find((item) => item.id == i.courseId),
            totalLectures: totalLectures,
            numOfStudent: numOfStudent,
            length: length,
          },
        ];
      }
      dispatch(setCourses(_courses));
      const listsRes = await axios
        .get(`http://localhost:8084/api/user-list/${baseLoad.user.userID}`)
        .then((res) => {
          _lists = res.data.map((item) => ({ ...item, courses: [] }));
        });
      dispatch(setLists(_lists));
    } catch (error) {
      console.log("cmm", error);
    }
  };
  useEffect(() => {
    fetchLearning();
  }, [baseLoad.user]);

  const listFormSubmit = async (e) => {
    e.preventDefault();
    try {
      let req = {
        name: listForm.title,
        description: listForm.description,
        userId: baseLoad.user.userID,
      };
      const res = await axios
        .post("http://localhost:8084/api/user-list/create", req)
        .then((res) => {
          console.log("base", baseLoad.learning);
          let _lists = [...baseLoad.learning.lists, res.data.id];
          let _learning = { ...baseLoad.learning, lists: _lists };
          console.log("learng", _learning);
          dispatch(setBaseLoad({ ...baseLoad, learning: _learning }));
          dispatch(setListInteraction({ status: "none", courseID: "" }));
          let _listForm = { ...listForm, title: "", description: "" };
          setListForm(_listForm);
        });
    } catch (error) {
      console.log(error);
    }
  };
  const listInteractionCancel = () => {
    if (listInteraction.status === "create") {
      let _listForm = { ...listForm, title: "", description: "" };
      setListForm(_listForm);
    }
    dispatch(setListInteraction({ status: "none", courseID: "" }));
  };
  const deleteListClicked = async (id) => {
    try {
      const res = await axios.delete(
        `http://localhost:8084/api/user-list/list/${id}/delete`
      );
      if (res.status == 200) {
        toast.success("List deleted!");
        let _lists = [...userLists];
        dispatch(setLists(_lists.filter((item) => item.id != id)));
      }
    } catch (error) {
      toast.error("Delete failed, something wrong!");
    }
  };
  return (
    <div className="my-learning-page">
      <div className="tab-selection-container">
        <div className="title">My learning</div>
        <div className="tab-selection-group">
          {tabSelected === "all-courses" ? (
            <div className="all-courses isActived">
              <span>All courses</span>
            </div>
          ) : (
            <div
              className="all-courses"
              onClick={() => {
                setTabSelected("all-courses");
              }}
            >
              <span>All courses</span>
            </div>
          )}
          {tabSelected === "my-lists" ? (
            <div className="my-lists isActived">
              <span>My lists</span>
            </div>
          ) : (
            <div
              className="my-lists"
              onClick={() => {
                setTabSelected("my-lists");
              }}
            >
              <span>My lists</span>
            </div>
          )}
        </div>
      </div>
      <div className="tab-content-container">
        <div className="tab-content">
          {tabSelected === "all-courses" ? (
            <div className="all-courses-content">
              {userCourses &&
                userCourses.map((course) => (
                  <CourseCardEnroll pageView={"all-courses"} course={course} />
                ))}
            </div>
          ) : (
            <div className="my-lists-content">
              {userLists &&
                userLists.map((item) => (
                  <div className="list-container" key={item.id}>
                    <div className="list-header">
                      <span>{item.name}</span>
                      <div className="btn-edit">
                        <MdEdit className="edit-icon" size={18} />
                      </div>
                      <div
                        className="btn-delete"
                        onClick={() => deleteListClicked(item.id)}
                      >
                        <MdDelete className="delete-icon" size={18} />
                      </div>
                    </div>
                    <div className="list-body">
                      {/* <CourseCardEnroll pageView={"my-lists"} courseID={""} /> */}
                    </div>
                  </div>
                ))}
            </div>
          )}
        </div>
      </div>
      <div
        className="list-interaction-container"
        style={listInteraction.status === "none" ? { display: "none" } : {}}
      >
        <form
          className="create-list-form"
          style={listInteraction.status !== "create" ? { display: "none" } : {}}
        >
          <span>Create new list</span>
          <div className="input-list-title">
            <input
              type="text"
              className=""
              placeholder="Title of list - max 30 characters"
              maxLength={30}
              required
              value={listForm.title}
              onChange={(e) => {
                setListForm({ ...listForm, title: e.target.value });
              }}
            />
          </div>
          <div className="input-list-description">
            <textarea
              name=""
              id=""
              placeholder="Desciption - max 60 characters"
              maxLength={60}
              required
              value={listForm.description}
              onChange={(e) => {
                setListForm({ ...listForm, description: e.target.value });
              }}
            ></textarea>
          </div>
          <div className="create-list-btn-group">
            <span onClick={() => listInteractionCancel()}>Cancel</span>
            <button type="submit" onClick={(e) => listFormSubmit(e)}>
              Create
            </button>
          </div>
        </form>

        <div
          className="add-to-list-form"
          style={
            listInteraction.status !== "add-to-list" ? { display: "none" } : {}
          }
        >
          <span>Add this course to list</span>
          <div className="_select-list">
            <select>
              <option value="">list 1</option>
              <option value="">list 2</option>
            </select>
          </div>
          <div className="add-to-list-btn-group">
            <span onClick={() => listInteractionCancel()}>Cancel</span>
            <button>Add</button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Learning;
