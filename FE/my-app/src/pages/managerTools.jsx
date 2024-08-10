import React, { useEffect, useState } from "react";
import "../styles/manager_tools.css";
import { BsSearch } from "react-icons/bs";
import { MdEdit, MdDelete } from "react-icons/md";
import { SlClose, SlPlus } from "react-icons/sl";
import { IoIosClose } from "react-icons/io";
import { toast } from "react-toastify";
import axios from "axios";

const ManagerTools = () => {
  const [toolSeleted, setToolSelected] = useState("course");
  const [popupDisplay, setPopupDisplay] = useState("");
  const [userForm, setUserForm] = useState({
    username: "",
    password: "",
    email: "",
    role: "",
  });
  const [users, setUsers] = useState([]);
  const [courseForm, setCourseForm] = useState("overview");
  const [findInstructor, setFindInstructor] = useState([]);
  const [instructor, setInstructor] = useState({
    id: 1,
    name: "",
  });
  const [categories, setCategories] = useState([
    { id: 3, name: "name3" },
    { id: 4, name: "name4" },
  ]);
  const [categoriesShow, setCategoriesShow] = useState([
    { id: 1, name: "name1" },
    { id: 2, name: "name2" },
  ]);
  const [categorySelected, setCategorySelected] = useState();
  const [overview, setOverview] = useState({
    id: null,
    title: "",
    description: "",
    price: 0,
    instructorId: null,
    instructorName: "",
    thumbnail: "",
    categoryIds: [1],
    requirements: "",
    summary: "",
    updateAt: "",
    createAt: "",
  });
  const [sections, setSections] = useState([]);
  const [lectures, setLectures] = useState([]);

  // useEffect(async () => {
  //   try {
  //     const coursesRes = await axios.get('http://localhost:8081/api/course')
  //     const categoriesRes = ""
  //   } catch (error) {

  //   }
  // },[])
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
      if (
        overview.title === "" ||
        overview.description === "" ||
        overview.instructorName === "" ||
        overview.thumbnail === "" ||
        overview.requirements === "" ||
        overview.summary === "" ||
        categoriesShow.length == 0
      ) {
        toast.warn("Misssing course information!");
      } else {
        console.log(overview);
        setCourseForm("section");
      }
    }
    if (courseForm === "section") {
      if (sections.length === 0) {
        toast.warn("Have no section!");
      } else {
        const sc = sections.filter((section) => section.title === "");
        if (sc.length > 0) {
          toast.warn("Fill title for section!");
        } else {
          if (lectures.length === 0) {
            const _lectures = sections.map((section) => ({
              section_position: section.position,
              section_title: section.title,
              lectures: [],
            }));
            setLectures(_lectures);
          }
          setCourseForm("lecture");
        }
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
  const courseFormBtnDoneClick = async (e) => {
    e.preventDefault();

    if (lectures.filter((item) => item.lectures.length == 0).length != 0) {
      toast.warn("Section need some lectures!");
    } else {
      let _lectures = [...lectures];
      _lectures.map((item) => item.lectures.map((lec) => delete lec.sectionId));
      let _sections = [
        ...sections.map((item) => ({
          id: item.id,
          title: item.title,
          position: item.position,
          lectures: _lectures.filter(
            (i) => i.section_position === item.position
          )[0].lectures,
        })),
      ];
      let _categoryIds = categoriesShow.map((item) => item.id);
      let _overview = { ...overview, categoryIds: _categoryIds };
      delete _overview.instructorName;
      let courseData = { ..._overview, sections: _sections };
      console.log(courseData);
      try {
        const res = await axios.post(
          "http://localhost:8081/api/course/save",
          courseData
        );
        if (res && res.data && res.data.message == "Success") {
          toast.success("Create success!");
          setCourseForm("overview");
          setPopupDisplay("");
        } else {
        }
      } catch (e) {
        toast.error("Create failed!");
        console.log(e);
      }
      console.log(courseData);
    }
  };

  const categorySelectedOnChange = (id) => {
    if (id == "none") {
      setCategorySelected("none");
    } else {
      let _cate = categories.filter((item) => item.id == id)[0];
      setCategorySelected(_cate);
    }
  };
  const removeCategoryFromShow = (id) => {
    let _categories = categoriesShow.filter((item) => item.id !== id);
    setCategoriesShow(_categories);
  };
  // handle course overview
  const courseOverviewOnChange = async (e, type) => {
    switch (type) {
      case "title":
        setOverview({ ...overview, title: e.target.value });
        break;
      case "description":
        setOverview({ ...overview, description: e.target.value });
        break;
      case "price":
        setOverview({ ...overview, price: e.target.valueAsNumber });
        break;
      case "instructor":
        try {
          setOverview({ ...overview, instructorName: e.target.value });
          const instructorRes = await axios.get(
            `http://localhost:8088/api/search/instructor?keyword=${e.target.value}`
          );
          if (instructorRes && instructorRes.data) {
            setFindInstructor(instructorRes.data);
          }
        } catch (error) {}

        setOverview({ ...overview, instructorName: e.target.value });
        break;
      case "thumbnail":
        let mineType =
          "data:image/" + e.target.value.split(".")[1] + ";base64, ";
        let reader = new FileReader();
        let file = e.target.files[0];
        if (file) {
          reader.onload = () => {
            let base64string = reader.result.split(",")[1];
            setOverview({ ...overview, thumbnail: mineType + base64string });
          };
          reader.readAsDataURL(file);
        }
        break;
      case "requirements":
        setOverview({ ...overview, requirements: e.target.value });
        break;
      case "summary":
        setOverview({ ...overview, summary: e.target.value });
        break;
      default:
    }
  };
  // handle sections
  const addSectionClick = () => {
    let section = {
      id: null,
      title: "",
      totalLectures: null,
      position: null,
      courseId: null,
    };
    if (sections.length === 0) {
      section.position = 1;
      const tmpSections = [section];
      setSections(tmpSections);
    } else {
      section.position = sections.length + 1;
      const tmpSections = [...sections, section];
      if (lectures.length > 0) {
        const tmpLectures = {
          section_position: tmpSections[tmpSections.length - 1].position,
          section_title: tmpSections[tmpSections.length - 1].title,
          lectures: [],
        };
        let _lectures = [...lectures, tmpLectures];
        setLectures(_lectures);
      }
      setSections(tmpSections);
    }
  };
  const handleChangeSection = (e, position) => {
    let tmpSections = [...sections];
    tmpSections.map((sc) =>
      sc.position == position ? (sc.title = e.target.value) : sc
    );
    let _lectures = [...lectures];
    _lectures.map((item) =>
      item.section_position === position
        ? (item.section_title = e.target.value)
        : item
    );
    setLectures(_lectures);
    setSections(tmpSections);
  };
  const removeSectionClick = (position) => {
    const tmpSections = sections.filter(
      (section) => section.position !== position
    );
    if (tmpSections.length === 0) {
      setSections([]);
    } else {
      tmpSections.map((section, index) => {
        if (section.position !== index + 1) {
          section.position = index + 1;
        }
      });
    }
    setSections(tmpSections);
    if (sections.length === 0) {
      setLectures([]);
    } else {
      const tmpLectures = lectures.filter(
        (lecture) => lecture.section_position !== position
      );
      tmpLectures.map((lecture, index) => {
        if (lecture.section_position !== index + 1) {
          lecture.section_position = index + 1;
        }
      });
      setLectures(tmpLectures);
    }
  };

  // handle lectures
  const addLectureClick = (section_position) => {
    const tmpLecture = {
      id: null,
      title: "",
      type: "video",
      value: "",
      length: null,
      position: null,
      sectionId: null,
    };

    const _lectures = [...lectures];
    _lectures.map((lec) => {
      if (lec.section_position === section_position) {
        if (lec.lectures.length === 0) {
          tmpLecture.position = 1;
          let lts = [tmpLecture];
          lec.lectures = lts;
        } else {
          tmpLecture.position = lec.lectures.length + 1;
          let lts = [...lec.lectures, tmpLecture];
          lec.lectures = lts;
        }
      }
    });
    setLectures(_lectures);
  };
  const removeLectureClick = (e, position) => {
    let parent_node = e.target.parentNode;
    for (let i = 0; i < 3; i++) {
      parent_node = parent_node.parentNode;
    }
    let section_position = parent_node.title;

    let _lectures = [...lectures];
    let _lecs = _lectures.filter(
      (item) => item.section_position == section_position
    );
    if (_lecs.length > 0) {
      _lecs = _lecs[0].lectures.filter((lec) => lec.position !== position);
      _lecs.map((lec, index) => {
        if (lec.position !== index + 1) {
          lec.position = index + 1;
        }
      });
    }
    if (_lecs.length === 0) {
      _lectures.map((item) =>
        item.section_position == section_position ? (item.lectures = []) : item
      );
    } else {
      _lectures.map((item) =>
        item.section_position == section_position
          ? (item.lectures = _lecs)
          : item
      );
    }
    setLectures(_lectures);
  };
  const handleLectureOnchange = (e, position, type) => {
    let parent_node = e.target.parentNode;
    for (let i = 0; i < 2; i++) {
      parent_node = parent_node.parentNode;
    }
    let section_position = parent_node.title;

    let _lectures = [...lectures];
    let _lecs = _lectures.filter(
      (item) => item.section_position == section_position
    )[0].lectures;
    switch (type) {
      case "title":
        _lecs.map((lec) =>
          lec.position == position ? (lec.title = e.target.value) : lec
        );
        _lectures.map((item) =>
          item.section_position == section_position
            ? (item.lectures = _lecs)
            : item
        );
        setLectures(_lectures);
        break;
      case "type":
        let _lec = _lecs.filter((lec) => lec.position === position)[0];
        if (e.target.value === "text" && _lec.value.length > 0) {
          try {
            const deleteVideoRes = axios.delete(
              `http://localhost:8080/videos/delete/${_lec.value}`
            );
            _lecs.map((lec) =>
              lec.position == position ? (lec.value = "") : lec
            );
          } catch (error) {}
        }
        _lecs.map((lec) =>
          lec.position == position ? (lec.type = e.target.value) : lec
        );
        _lectures.map((item) =>
          item.section_position == section_position
            ? (item.lectures = _lecs)
            : item
        );
        setLectures(_lectures);
        break;
      case "value-text":
        _lecs.map((lec) =>
          lec.position == position ? (lec.value = e.target.value) : lec
        );
        _lectures.map((item) =>
          item.section_position == section_position
            ? (item.lectures = _lecs)
            : item
        );
        setLectures(_lectures);
        break;
      case "length":
        console.log(lectures);
        _lecs.map((lec) =>
          lec.position == position
            ? (lec[`length`] = e.target.valueAsNumber)
            : lec
        );
        _lectures.map((item) =>
          item.section_position == section_position
            ? (item.lectures = _lecs)
            : item
        );
        setLectures(_lectures);
        break;
      default:
    }
  };

  //handle video iput
  const handleVideoChange = async (e, position) => {
    let parent_node = e.target.parentNode;
    for (let i = 0; i < 2; i++) {
      parent_node = parent_node.parentNode;
    }
    let section_position = parent_node.title;

    let _lectures = [...lectures];
    let _lecs = _lectures.filter(
      (item) => item.section_position == section_position
    )[0].lectures;

    try {
      let _lec = _lecs.filter((lec) => lec.position === position)[0];
      if (_lec.value.length > 0) {
        const deleteVideoRes = await axios.delete(
          `http://localhost:8080/videos/delete/${_lec.value}`
        );
        if (deleteVideoRes && deleteVideoRes.data) {
          console.log(deleteVideoRes.data.message);
          _lecs.map((lec) =>
            lec.position == position ? (lec.value = "") : lec
          );
          _lectures.map((item) =>
            item.section_position == section_position
              ? (item.lectures = _lecs)
              : item
          );
          setLectures(_lectures);
        }
      }
      let formData = new FormData();
      formData.append("file", e.target.files[0]);
      const res = await axios.post(
        "http://localhost:8080/videos/add",
        formData
      );
      if (res && res.data) {
        console.log(res);
        _lecs.map((lec) =>
          lec.position == position ? (lec.value = res.data.id) : lec
        );
        _lectures.map((item) =>
          item.section_position == section_position
            ? (item.lectures = _lecs)
            : item
        );
        setLectures(_lectures);
      }
    } catch (error) {}
  };

  //----------------------------------
  //user form handle
  const userFormOnchange = (e, type) => {
    let _userForm = { ...userForm };
    switch (type) {
      case "username":
        _userForm.username = e.target.value;
        setUserForm(_userForm);
        break;
      case "password":
        _userForm.password = e.target.value;
        setUserForm(_userForm);
        break;
      case "email":
        _userForm.email = e.target.value;
        setUserForm(_userForm);
        break;
      case "role":
        _userForm.role = e.target.value;
        setUserForm(_userForm);
        break;
    }
  };
  const userFormBtnOnClick = async () => {
    try {
      const res = await axios.post(
        "http://localhost:8082/api/user/save",
        userForm
      );
      res && res.message
        ? console.log(res.message)
        : console.log("khong thanh cong");
    } catch (error) {}
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
              <form onSubmit={(e) => courseFormBtnDoneClick(e)}>
                {/* overview form */}
                <div
                  className="_course-form-overview"
                  style={courseForm !== "overview" ? { display: "none" } : {}}
                >
                  <div>
                    <span>Title</span>
                    <input
                      type="text"
                      value={overview.title}
                      placeholder="title"
                      onChange={(e) => courseOverviewOnChange(e, "title")}
                    />
                  </div>
                  <div>
                    <span>Description</span>
                    <input
                      type="text"
                      value={overview.description}
                      placeholder="description"
                      onChange={(e) => courseOverviewOnChange(e, "description")}
                    />
                  </div>
                  <div>
                    <span>Price</span>
                    <input
                      type="number"
                      min={0}
                      value={overview.price}
                      placeholder="price"
                      onChange={(e) => courseOverviewOnChange(e, "price")}
                    />
                  </div>
                  <div>
                    <span>Instructor</span>
                    <div
                      style={{
                        display: "flex",
                        flexWrap: "wrap",
                        position: "relative",
                      }}
                    >
                      <input
                        type="text"
                        value={overview.instructorName}
                        placeholder="instructor"
                        onChange={(e) =>
                          courseOverviewOnChange(e, "instructor")
                        }
                      />
                      <div
                        style={
                          findInstructor.length > 0
                            ? {
                                maxWidth: "375px",
                                marginTop: "3px",
                                height: "fit-content !important",
                                position: "absolute",
                                zIndex: "1",
                                top: "20px",
                                display: "flex",
                                flexWrap: "wrap",
                                backgroundColor: "white",
                                border: "1px solid grey",
                              }
                            : { display: "none" }
                        }
                      >
                        {findInstructor.length > 0 ? (
                          findInstructor.map((i) => (
                            <div
                              key={i.instructorId}
                              style={{
                                height: "20px",
                                paddingLeft: "5px",
                                borderBottom: "1px solid grey",
                                margin: "0",
                                cursor: "pointer",
                              }}
                              onClick={() => {
                                setOverview({
                                  ...overview,
                                  instructorId: i.instructorId,
                                  instructorName: i.name,
                                });
                                setFindInstructor([]);
                              }}
                            >
                              <span
                                style={{
                                  width: "fit-content",
                                  marginRight: "5px",
                                }}
                              >
                                {i.instructorId} :
                              </span>
                              <span>{i.name}</span>
                            </div>
                          ))
                        ) : (
                          <></>
                        )}
                      </div>
                    </div>
                  </div>
                  <div>
                    <span>Thumbnail</span>
                    <input
                      type="file"
                      id="thumbnail"
                      accept="image/*"
                      onChange={(e) => courseOverviewOnChange(e, "thumbnail")}
                    />
                  </div>
                  <div>
                    <span>Categories</span>
                    <div style={{ display: "flex", flexWrap: "wrap" }}>
                      {categoriesShow.length > 0 ? (
                        categoriesShow.map((item) => (
                          <div key={item.id}>
                            <span style={{ width: "fit-content" }}>
                              {item.name}
                            </span>
                            <IoIosClose
                              size={18}
                              title="remove this category"
                              style={{ cursor: "pointer" }}
                              onClick={() => removeCategoryFromShow(item.id)}
                            />
                          </div>
                        ))
                      ) : (
                        <span>none</span>
                      )}
                      <div>
                        <select
                          style={{ width: "150px" }}
                          onChange={(e) => {
                            categorySelectedOnChange(e.target.value);
                          }}
                        >
                          <option value={"none"}>none</option>
                          {categories && categories.length > 0 ? (
                            categories.map((category) => (
                              <option key={category.id} value={category.id}>
                                {category.name}
                              </option>
                            ))
                          ) : (
                            <></>
                          )}
                        </select>
                        <div
                          style={{
                            display: "flex",
                            alignItems: "center",
                            padding: "5px",
                            marginLeft: "5px",
                            cursor: "pointer",
                          }}
                          onClick={(e) => {
                            e.preventDefault();
                            if (categorySelected !== "none") {
                              categoriesShow.filter(
                                (item) => item.id == categorySelected.id
                              ).length > 0
                                ? toast.warn("Category has been added")
                                : setCategoriesShow([
                                    ...categoriesShow,
                                    categorySelected,
                                  ]);
                            }
                          }}
                        >
                          <SlPlus size={18} />
                        </div>
                      </div>
                    </div>
                  </div>
                  <div>
                    <span>Requirements</span>
                    <textarea
                      value={overview.requirements}
                      placeholder="requirements"
                      style={{ height: "100px" }}
                      onChange={(e) =>
                        courseOverviewOnChange(e, "requirements")
                      }
                    ></textarea>
                  </div>
                  <div>
                    <span>Summary</span>
                    <textarea
                      value={overview.summary}
                      placeholder="summary"
                      style={{ height: "200px" }}
                      onChange={(e) => courseOverviewOnChange(e, "summary")}
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
                  {lectures.map((item) => (
                    <div
                      className="_section-lectures-input"
                      key={item.section_position}
                      title={item.section_position}
                    >
                      <div className="_lecture-section-title">
                        {`Section ${item.section_position}: ${item.section_title}`}
                      </div>
                      {item.lectures.map((lec) => (
                        <div className="_lecture-input" key={lec.position}>
                          <div className="_lecture-input-header">
                            <span
                              style={{
                                textDecoration: "underline",
                                textUnderlineOffset: "4px",
                              }}
                            >
                              - Lecture {lec.position}
                            </span>
                            <MdDelete
                              title="remove this lecture"
                              onClick={(e) =>
                                removeLectureClick(e, lec.position)
                              }
                            />
                          </div>
                          <div>
                            <span>Title</span>
                            <input
                              type="text"
                              required
                              placeholder="title"
                              value={lec.title}
                              onChange={(e) =>
                                handleLectureOnchange(e, lec.position, "title")
                              }
                            />
                          </div>
                          <div>
                            <span>Type</span>
                            <select
                              onChange={(e) =>
                                handleLectureOnchange(e, lec.position, "type")
                              }
                            >
                              <option value="video">Video</option>
                              <option value="text">Text</option>
                            </select>
                          </div>
                          <div>
                            <span>Source</span>
                            {lec.type === "video" ? (
                              <input
                                required
                                type="file"
                                name=""
                                id="lecture-source"
                                accept=".mp4,.pdf"
                                onChange={(e) =>
                                  handleVideoChange(e, lec.position)
                                }
                              />
                            ) : (
                              <textarea
                                required
                                value={lec.value}
                                onChange={(e) =>
                                  handleLectureOnchange(
                                    e,
                                    lec.position,
                                    "value-text"
                                  )
                                }
                              />
                            )}
                          </div>
                          <div>
                            <span>Length</span>
                            <input
                              type="number"
                              required
                              min={0.05}
                              step={0.05}
                              onChange={(e) =>
                                handleLectureOnchange(e, lec.position, "length")
                              }
                            />
                          </div>
                        </div>
                      ))}

                      <div
                        className="_section-lectures-add"
                        onClick={() => addLectureClick(item.section_position)}
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
                  {courseForm !== "lecture" ? (
                    <button onClick={(e) => courseFormBtnClick(e)}>Next</button>
                  ) : (
                    <button type="submit">Done</button>
                  )}
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
                  <input
                    type="text"
                    placeholder="username"
                    value={userForm.username}
                    onChange={(e) => userFormOnchange(e, "username")}
                  />
                </div>
                <div className="password">
                  <input
                    type="text"
                    placeholder="password"
                    value={userForm.password}
                    onChange={(e) => userFormOnchange(e, "password")}
                  />
                </div>
                <div className="email">
                  <input
                    type="text"
                    placeholder="email"
                    value={userForm.email}
                    onChange={(e) => userFormOnchange(e, "email")}
                  />
                </div>
                <div className="role">
                  <span>Role: </span>
                  <select
                    id=""
                    value={userForm.role}
                    onChange={(e) => userFormOnchange(e, "role")}
                  >
                    <option value="manager">manager</option>
                    <option value="admin">admin</option>
                  </select>
                </div>
                <div className="_btn-popup-create">
                  <button type="submit">
                    {popupDisplay === "create-user" ? "Create" : "Save"}
                  </button>
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
