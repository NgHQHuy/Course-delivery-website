import React, { useState } from "react";
import "../styles/manager_tools.css";
import { BsSearch } from "react-icons/bs";
import { MdEdit, MdDelete } from "react-icons/md";
import { SlPlus } from "react-icons/sl";
import { IoIosClose } from "react-icons/io";

const ManagerTools = () => {
  const [toolSeleted, setToolSelected] = useState("course");
  const [popupDisplay, setPopupDisplay] = useState({ display: "none" });
  const toolClick = (tool) => {
    setToolSelected(tool);
  };
  return (
    <div className="manager-page">
      <div className="tools-group">
        <div
          className={toolSeleted === "course" ? "course isActived" : "course"}
          onClick={() => toolClick("course")}
        >
          Course
        </div>
        <div
          className={toolSeleted === "team" ? "team isActived" : "team"}
          onClick={() => toolClick("team")}
        >
          Team
        </div>
      </div>
      <div className="tool-panel">
        {toolSeleted === "course" ? (
          <div className="course-management">
            <div className="search-tool-container">
              <div className="_search-by">
                <span>Search by</span>
                <select className="_search-by-selection">
                  <option value="_id">ID</option>
                  <option value="_name">Name</option>
                </select>
              </div>
              <input type="text" name="_search-box" placeholder="Input here" />
              <div className="_btn-search">
                <BsSearch />
              </div>
            </div>
            <div className="board">
              <div className="board-item">
                <div className="_item-id">
                  <span>1</span>
                </div>
                <div className="_item-name">
                  <span>course title</span>
                </div>
                <div className="_item-btn-group">
                  <div className="_btn-edit-item">
                    <MdEdit />
                  </div>
                  <div className="_btn-delete-item">
                    <MdDelete />
                  </div>
                </div>
              </div>
            </div>
            <div className="btn-tool-group">
              <div className="_btn-create">
                <SlPlus />
                <span>New</span>
              </div>
            </div>
          </div>
        ) : (
          <div className="team-management">
            <div className="search-tool-container">
              <div className="_search-by">
                <span>Search by</span>
                <select className="_search-by-selection">
                  <option value="_id">ID</option>
                  <option value="_name">Name</option>
                </select>
              </div>
              <input type="text" name="_search-box" placeholder="Input here" />
              <div className="_btn-search">
                <BsSearch />
              </div>
            </div>
            <div className="role-selection">
              <span>Filter by</span>
              <select className="_role-selection">
                <option value="admin">admin</option>
                <option value="manager">manager</option>
              </select>
            </div>
            <div className="board">
              <div className="board-item">
                <div className="_item-id">
                  <span>1</span>
                </div>
                <div className="_item-name">
                  <span>user name</span>
                </div>
                <div className="_item-role">
                  <span>manager</span>
                </div>
                <div className="_item-btn-group">
                  <div className="_btn-edit-item">
                    <MdEdit />
                  </div>
                  <div className="_btn-delete-item">
                    <MdDelete />
                  </div>
                </div>
              </div>
            </div>
            <div className="btn-tool-group">
              <div className="_btn-create" onClick={() => setPopupDisplay({})}>
                <SlPlus />
                <span>New</span>
              </div>
            </div>
          </div>
        )}
      </div>
      <div className="tool-popup" style={popupDisplay}>
        <div className="_popup-create-user">
          <div className="_close-popup">
            <div
              className="_close-popup-icon"
              onClick={() => setPopupDisplay({ display: "none" })}
            >
              <IoIosClose size={28} />
            </div>
          </div>
          <div className="_popup-title">
            <span>Create user</span>
          </div>
          <div className="_popup-form-user">
            <form action="">
              <div className="username">
                <input type="text" placeholder="username" />
              </div>
              <div className="password">
                <input type="text" placeholder="password" />
              </div>
              <div className="email">
                <input type="text" placeholder="email" />
              </div>
              <div className="role">
                <span>Role: </span>
                <select id="">
                  <option value="manager">manager</option>
                  <option value="admin">admin</option>
                </select>
              </div>
              <div className="_btn-popup-create">
                <button type="submit">Create</button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ManagerTools;
