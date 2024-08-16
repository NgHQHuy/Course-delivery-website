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
import { useNavigate } from "react-router-dom";

const Learning = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const baseLoad = useSelector(getBaseLoad);
  const allCourses = useSelector(getListAllCourses);
  const listInteraction = useSelector(getListInteraction);
  const userCourses = useSelector(getCourses);
  const userLists = useSelector(getLists);

  const [tabSelected, setTabSelected] = useState("all-courses");
  const [listForm, setListForm] = useState({
    id: null,
    name: "",
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
          let _tmpLists = res.data.map((item) => ({ ...item, courses: [] }));
          _tmpLists.map((i, index) => {
            const _courses = axios
              .get(`http://localhost:8084/api/user-list/list/${i.id}/courses`)
              .then((res) => {
                if (res.data && res.data.length > 0) {
                  let _tmpCourses = res.data.map((c) => c.courseId);
                  i = { ...i, courses: _tmpCourses };
                  _lists = [..._lists, i];
                  dispatch(setLists(_lists));
                } else {
                  i = { ...i, courses: [] };
                  dispatch(setLists([..._lists, i]));
                }
              });
          });
        });
    } catch (error) {}
  };
  const deleteCourseFromList = async (listId, courseId) => {
    try {
      let req = { listId: listId, courseId: courseId };
      const res = await axios.delete(
        "http://localhost:8084/api/user-list/deleteCourse",
        { data: req }
      );
      if (res && res.status == 200) {
        let _userlist = [...userLists];
        let _courses = _userlist.find((item) => item.id == listId).courses;
        _courses = _courses.filter((id) => id != courseId);
        _userlist = _userlist.map((item) =>
          item.id == listId ? { ...item, courses: _courses } : item
        );
        dispatch(setLists(_userlist));
        dispatch(
          setListInteraction({ status: "none", listId: null, courseId: null })
        );
      }
    } catch (error) {
      toast.error("Delete failed!");
    }
  };
  useEffect(() => {
    if (
      window.sessionStorage.getItem("_uid") &&
      window.sessionStorage.getItem("_role") == "USER"
    )
      fetchLearning();
    else navigate("/");
    if (listInteraction.status == "delete-course-from-list") {
      deleteCourseFromList(listInteraction.listId, listInteraction.courseId);
    }
  }, [listInteraction]);

  const listInteractionCancel = () => {
    let _listForm = { id: null, name: "", description: "" };
    setListForm(_listForm);
    dispatch(setListInteraction({ status: "none" }));
  };
  const editListClicked = (list) => {
    setListForm(list);
    dispatch(setListInteraction({ status: "edit" }));
  };
  const listFormSubmit = async (e) => {
    e.preventDefault();
    if (listInteraction.status == "create") {
      try {
        let req = {
          name: listForm.name,
          description: listForm.description,
          userId: baseLoad.user.userID,
        };
        const res = await axios
          .post("http://localhost:8084/api/user-list/create", req)
          .then((res) => {
            dispatch(setListInteraction({ status: "none", courseID: "" }));
            let _listForm = { ...listForm, name: "", description: "" };
            setListForm(_listForm);
          });
      } catch (error) {
        console.log(error);
      }
    }

    if (listInteraction.status == "edit") {
      console.log("rea", typeof listForm.id);
      let _l = {
        listId: listForm.id,
        name: listForm.name,
        description: listForm.description,
      };
      try {
        const res = await axios.post(
          "http://localhost:8084/api/user-list/update",
          _l
        );
        if (res.status == 200) {
          let _listUpdated = userLists.map((item) =>
            item.id == listForm.id
              ? {
                  ...item,
                  name: listForm.name,
                  description: listForm.description,
                }
              : item
          );
          toast.success("Edit success!");
          dispatch(setLists(_listUpdated));
          setListForm({ id: null, name: "", description: "" });
          dispatch(setListInteraction({ status: "none" }));
        }
      } catch (error) {
        console.log("edit errr", error);
      }
    }
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
                      <div
                        className="btn-edit"
                        onClick={() =>
                          editListClicked({
                            id: item.id,
                            name: item.name,
                            description: item.description,
                          })
                        }
                      >
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
                      {item.courses.length > 0 ? (
                        item.courses.map((course) => (
                          <CourseCardEnroll
                            pageView={"my-lists"}
                            course={userCourses.find((i) => i.id == course)}
                            listId={item.id}
                          />
                        ))
                      ) : (
                        <span style={{ fontStyle: "italic" }}>
                          This list is empty.
                        </span>
                      )}
                    </div>
                  </div>
                ))}
            </div>
          )}
        </div>
      </div>
      <div
        className="list-interaction-container"
        style={
          listInteraction.status === "create" ||
          listInteraction.status === "edit"
            ? {}
            : { display: "none" }
        }
      >
        <form className="create-list-form">
          <span>Create new list</span>
          <div className="input-list-title">
            <input
              type="text"
              className=""
              placeholder="Title of list - max 30 characters"
              maxLength={30}
              required
              value={listForm.name}
              onChange={(e) => {
                setListForm({ ...listForm, name: e.target.value });
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
              {listInteraction.status == "create" ? "Create" : "Edit"}
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
