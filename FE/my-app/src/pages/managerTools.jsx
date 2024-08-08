import React, { useState } from "react";
import "../styles/manager_tools.css";
import { BsSearch } from "react-icons/bs";
import { MdEdit, MdDelete } from "react-icons/md";
import { SlPlus } from "react-icons/sl";
import { IoIosClose } from "react-icons/io";
import { toast } from "react-toastify";

const ManagerTools = () => {
  const [toolSeleted, setToolSelected] = useState("course");
  const [popupDisplay, setPopupDisplay] = useState("");
  const [courseForm, setCourseForm] = useState("overview");
  const [overview, setOverview] = useState();
  const [sections, setSections] = useState([]);
  const [lectures, setLectures] = useState([]);

  const toolClick = (tool) => {
    setToolSelected(tool);
  };
  const closePopup = () => {
    setPopupDisplay("");
    if (popupDisplay === "create-course") {
      setCourseForm("overview");
    }
  };
  const editBtnClick = (type) => {
    setPopupDisplay(type);
  };
  const deleteBtnClick = (type) => {};
  const courseFormBtnClick = (e) => {
    e.preventDefault();
    if (courseForm === "overview") {
      setCourseForm("section");
    }
    if (courseForm === "section") {
      const sc = sections.filter((section) => section.title === "");
      if (sc.length > 0) {
        toast.warn("Fill title for section!");
      } else {
        if (lectures.length === 0) {
          const _lectures = sections.map((section) => ({
            section_position: section.position,
            lectures: [],
          }));
          setLectures(_lectures);
        }
        setCourseForm("lecture");
      }
    }
    if (courseForm === "lecture") {
      setCourseForm("overview");
    }
  };
  const courseFormBackClick = () => {
    if (courseForm === "section") {
      setCourseForm("overview");
    }
    if (courseForm === "lecture") {
      setCourseForm("section");
    }
  };

  // handle sections
  const addSectionClick = () => {
    let section = {
      sectionID: null,
      title: "",
      totalLectures: null,
      position: null,
      courseID: "",
    };
    if (sections.length === 0) {
      section.position = 1;
      const tmpSections = [section];
      setSections(tmpSections);
    } else {
      section.position = sections.length + 1;
      const tmpSections = [...sections, section];
      setSections(tmpSections);
    }
  };
  const handleChangeSection = (e, position) => {
    let tmpSections = [...sections];
    tmpSections.map((sc) =>
      sc.position == position ? (sc.title = e.target.value) : sc
    );
    setSections(tmpSections);
  };
  const removeSectionClick = (position) => {
    const tmpSections = sections.filter(
      (section) => section.position !== position
    );
    tmpSections.map((section, index) => {
      if (section.position !== index + 1) {
        section.position = index + 1;
      }
    });
    const tmpLectures = lectures.filter(
      (lecture) => lecture.section_position !== position
    );
    tmpLectures.map((lecture, index) => {
      if (lecture.section_position !== index + 1) {
        lecture.section_position = index + 1;
      }
    });
    setSections(tmpSections);
    setLectures(tmpLectures);
  };

  // handle lectures
  const addLectureClick = (section_position) => {
    console.log(lectures);
    // const tmpLecture = {
    //   lectureID: "",
    //   title: "",
    //   type: "",
    //   value: "",
    //   length: null,
    //   position: null,
    //   sectionID: "",
    // };
    // if (lectures.length === 0) {
    //   tmpLecture.position = 1;
    //   const tmpLectures = {
    //     section_position: section_position,
    //     lectures: [{ ...tmpLecture }],
    //   };
    //   const _lectures = [tmpLectures];
    //   setLectures(_lectures);
    // }
    // console.log(lectures);
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
                  <div
                    className="_btn-edit-item"
                    onClick={() => editBtnClick("edit-course")}
                  >
                    <MdEdit />
                  </div>
                  <div
                    className="_btn-delete-item"
                    onClick={() => deleteBtnClick("delete-course")}
                  >
                    <MdDelete />
                  </div>
                </div>
              </div>
            </div>
            <div className="btn-tool-group">
              <div
                className="_btn-create"
                onClick={() => setPopupDisplay("create-course")}
              >
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
                <option value="all">all</option>
                <option value="client">client</option>
                <option value="manager">manager</option>
                <option value="admin">admin</option>
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
                  <div
                    className="_btn-edit-item"
                    onClick={() => editBtnClick("edit-user")}
                  >
                    <MdEdit />
                  </div>
                  <div
                    className="_btn-delete-item"
                    onClick={() => deleteBtnClick("delete-user")}
                  >
                    <MdDelete />
                  </div>
                </div>
              </div>
            </div>
            <div className="btn-tool-group">
              <div
                className="_btn-create"
                onClick={() => setPopupDisplay("create-user")}
              >
                <SlPlus />
                <span>New</span>
              </div>
            </div>
          </div>
        )}
      </div>
      <div
        className="tool-popup"
        style={popupDisplay === "" ? { display: "none" } : {}}
        onClick={(e) => {
          if (e.target.matches("._close-popup-icon")) {
            closePopup();
          }
        }}
      >
        {popupDisplay === "create-course" || popupDisplay === "edit-course" ? (
          <div className="_popup-create-course">
            <div className="_close-popup">
              <div className="_close-popup-icon">
                <IoIosClose size={28} />
              </div>
            </div>
            <div className="_popup-course-container">
              <div className="_popup-course-container-title">
                {popupDisplay === "create-course"
                  ? "Create course"
                  : "Edit course"}
              </div>
              <form>
                {/* overview form */}
                <div
                  className="_course-form-overview"
                  style={courseForm !== "overview" ? { display: "none" } : {}}
                >
                  <div>
                    <span>Title</span>
                    <input
                      type="text"
                      onChange={() => {}}
                      placeholder="title"
                    />
                  </div>
                  <div>
                    <span>Description</span>
                    <input
                      type="text"
                      onChange={() => {}}
                      placeholder="description"
                    />
                  </div>
                  <div>
                    <span>Price</span>
                    <input
                      type="number"
                      min={0}
                      step={1000}
                      onChange={() => {}}
                      placeholder="price"
                    />
                  </div>
                  <div>
                    <span>Instructor</span>
                    <input
                      type="text"
                      onChange={() => {}}
                      placeholder="instructor"
                    />
                  </div>
                  <div>
                    <span>Thumbnail</span>
                    <input type="file" id="thumbnail" accept="image/*" />
                  </div>
                  <div>
                    <span>Requirement</span>
                    <textarea
                      placeholder="requirement"
                      style={{ height: "100px" }}
                    ></textarea>
                  </div>
                  <div>
                    <span>Summary</span>
                    <textarea
                      placeholder="summary"
                      style={{ height: "200px" }}
                    ></textarea>
                  </div>
                </div>
                {/* section form */}
                <div
                  className="_course-form-sections"
                  style={courseForm !== "section" ? { display: "none" } : {}}
                >
                  <div className="form-sections-input">
                    {sections.length > 0 ? (
                      sections.map((section) => (
                        <div className="_section" key={section.position}>
                          <span>Section {section.position}</span>
                          <input
                            type="text"
                            placeholder="title"
                            value={section.title}
                            onChange={(e) =>
                              handleChangeSection(e, section.position)
                            }
                          />
                          <div
                            className="_section-btn-remove"
                            title="remove"
                            onClick={() => removeSectionClick(section.position)}
                          >
                            <MdDelete />
                          </div>
                        </div>
                      ))
                    ) : (
                      <></>
                    )}
                  </div>
                  <div
                    className="form-sections-add"
                    onClick={() => addSectionClick()}
                  >
                    <SlPlus size={25} />
                    <span>section</span>
                  </div>
                </div>
                {/* lecture form */}
                <div
                  className="_course-form-lectures"
                  style={courseForm !== "lecture" ? { display: "none" } : {}}
                >
                  {sections.map((section) => (
                    <div
                      className="_section-lectures-input"
                      key={section.position}
                    >
                      <div className="_lecture-section-title">
                        {`Section ${section.position}: ${section.title}`}
                      </div>
                      <div className="_lecture-input">
                        <div className="_lecture-input-header">
                          <span>- Lecture</span>
                          <MdDelete title="remove this lecture" />
                        </div>
                        <div>
                          <span>Title</span>
                          <input type="text" placeholder="title" />
                        </div>
                        <div>
                          <span>Type</span>
                          <select>
                            <option value="lecture-type-text">Text</option>
                            <option value="lecture-type-video">Video</option>
                          </select>
                        </div>
                        <div>
                          <span>Source</span>
                          <input
                            type="file"
                            name=""
                            id="lecture-source"
                            accept=".mp4,.pdf"
                          />
                        </div>
                        <div>
                          <span>Length</span>
                          <input type="number" min={0.05} step={0.05} />
                        </div>
                      </div>

                      <div
                        className="_section-lectures-add"
                        onClick={() => addLectureClick(section.position)}
                      >
                        <SlPlus />
                        <span>lecture</span>
                      </div>
                    </div>
                  ))}
                </div>

                <div className="_popup-course-form-btn">
                  <span
                    style={
                      courseForm === "overview" ? { visibility: "hidden" } : {}
                    }
                    onClick={() => courseFormBackClick()}
                  >
                    Back
                  </span>
                  <button onClick={(e) => courseFormBtnClick(e)}>Next</button>
                </div>
              </form>
            </div>
          </div>
        ) : (
          <></>
        )}
        {popupDisplay === "create-user" || popupDisplay === "edit-user" ? (
          <div className="_popup-create-user">
            <div className="_close-popup">
              <div className="_close-popup-icon">
                <IoIosClose size={28} />
              </div>
            </div>
            <div className="_popup-title">
              <span>
                {popupDisplay === "create-user" ? "Create user" : "Edit user"}
              </span>
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
        ) : (
          <></>
        )}
      </div>
    </div>
  );
};

export default ManagerTools;
