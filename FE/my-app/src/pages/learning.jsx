import React, { useEffect, useState } from "react";
import "../styles/learning.css";
import CourseCardEnroll from "../components/courseCardEnroll";
import { MdEdit, MdDelete } from "react-icons/md";

import {
  setListInteraction,
  getListInteraction,
} from "../redux/learning.slice";
import { useDispatch, useSelector } from "react-redux";

const Learning = () => {
  const dispatch = useDispatch();
  const listInteraction = useSelector(getListInteraction);

  const [tabSelected, setTabSelected] = useState("all-courses");
  const [listForm, setListForm] = useState({
    id: null,
    title: "",
    description: "",
  });

  const fetchUserCourses = async () => {};
  const fetchUserLists = async () => {};
  useEffect(() => {
    fetchUserCourses();
    fetchUserLists();
  }, []);
  const listFormSubmit = (e) => {
    e.preventDefault();
    console.log(listInteraction);
    console.log("form: ", listForm);
    dispatch(setListInteraction({ status: "none", courseID: "" }));
    let _listForm = { ...listForm, title: "", description: "" };
    setListForm(_listForm);
  };
  const listInteractionCancel = () => {
    if (listInteraction.status === "create") {
      let _listForm = { ...listForm, title: "", description: "" };
      setListForm(_listForm);
    }
    dispatch(setListInteraction({ status: "none", courseID: "" }));
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
              <CourseCardEnroll pageView={"all-courses"} courseID={1} />
              <CourseCardEnroll pageView={"all-courses"} courseID={2} />
              <CourseCardEnroll pageView={"all-courses"} />
              <CourseCardEnroll pageView={"all-courses"} />
              <CourseCardEnroll pageView={"all-courses"} />
            </div>
          ) : (
            <div className="my-lists-content">
              <div className="list-container">
                <div className="list-header">
                  <span>list title here</span>
                  <div className="btn-edit">
                    <MdEdit className="edit-icon" size={18} />
                  </div>
                  <div className="btn-delete">
                    <MdDelete className="delete-icon" size={18} />
                  </div>
                </div>
                <div className="list-body">
                  <CourseCardEnroll pageView={"my-lists"} />
                </div>
              </div>

              <div className="list-container">
                <div className="list-header">
                  <span>list title here</span>
                  <div className="btn-edit">
                    <MdEdit className="edit-icon" size={18} />
                  </div>
                  <div className="btn-delete">
                    <MdDelete className="delete-icon" size={18} />
                  </div>
                </div>
                <div className="list-body">
                  <CourseCardEnroll pageView={"my-lists"} courseID={""} />
                </div>
              </div>
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
