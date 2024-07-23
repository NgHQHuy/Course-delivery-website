import React, { useEffect, useState } from "react";
import "../styles/course_content.css"
import { FaAnglesLeft, FaAngleDown } from "react-icons/fa6";
import { IoIosClose, IoIosStar, IoIosAlert } from "react-icons/io";
import { GoVideo } from "react-icons/go";

import axios from "axios"

const CourseContent = () => {
    const [menuContent, setMenuContent] = useState("")
    const [sectionShow, setSectionShow] = useState("show")
    const [tabSelected, setTabSelected] = useState("overview")

    const [courseData, setCourseData] = useState({})
    const [count, setCount] = useState(0)
    const getData =async()=> {
        const data = await axios.get('http://localhost:8081/api/course/1').then()
        setCourseData(data.data)
    }
    useEffect(()=>{
        getData()
    },[])

    const setMenuStatus =()=> {
        menuContent === "" ? setMenuContent("active") : setMenuContent("")
    }
    const sectionContentShow =()=>{
        sectionShow === "" ? setSectionShow("show") : setSectionShow("")
    }
    const tabSelectionClick =(tab)=> {
        setTabSelected(tab)
    }
    return (
        <div className="course-content">
            <div className="column-display">
                {menuContent === "" ? 
                    <div className="btn-menu-slide" onClick={()=>setMenuStatus()}>
                        <div className="icon"><FaAnglesLeft size={20}/></div>
                        <div className="text"><span>Course content</span></div>
                    </div>
                    :
                    <></>
                }
                <div className="course-content-media" onContextMenu={(e)=>{e.preventDefault()}}>
                    <video autoPlay controls controlsList="nodownload">
                        <source src="http://localhost:8080/videos/stream/6698a6fa1580bb00f93f71ef.mp4" type="video/mp4"/>
                    </video>
                </div>
                <div className="course-content-other">
                    <div className="tab-selection">
                        {tabSelected === "overview" ?
                            <div className="overview tabActived"><span>Overview</span></div>
                        :
                            <div className="overview" onClick={()=>tabSelectionClick("overview")}>Overview</div>
                        }
                        {tabSelected === "reviews" ?
                            <div className="reviews tabActived"><span>Reviews</span></div>
                        :
                            <div className="reviews" onClick={()=>tabSelectionClick("reviews")}>Reviews</div>
                        }
                    </div>
                    <div className="tab-content">
                        {tabSelected === "overview" ? 
                            <div className="overview-content">
                                <div className="overview-content-info">
                                    <div className="title">
                                        {courseData.title}
                                    </div>
                                    <div className="by-number">
                                        <div className="rating">
                                            <div className="by-number-group">
                                                <span>4.5</span> <IoIosStar size={20}/>
                                            </div>
                                            40,242 ratings
                                        </div>
                                        <div className="num-of-students">
                                            <div className="by-number-group">
                                                <span>741,272</span>
                                            </div>
                                            Students
                                        </div>
                                        <div className="total-hours">
                                            <div className="by-number-group">
                                                <span>2.5 hours</span>
                                            </div>
                                            Total
                                        </div>
                                    </div>
                                    <div className="last-updated">
                                        <IoIosAlert size={20}/> <span>Last updated 24/06/2024</span>
                                    </div>
                                </div>
                                <div className="overview-content-description">
                                    <div className="grid-grow-1-d">Description:</div>
                                    <div className="grid-grow-2-d">
                                        <div className="description-text">
                                            <p>                                  
                                                {courseData.description}
                                            </p>
                                        </div>
                                    </div>
                                </div>
                                <div className="overview-content-instructor">
                                    <div className="grid-grow-1-i"></div>
                                    <div className="grid-grow-2-i"></div>
                                </div>
                            </div>
                            :
                            <div className="reviews-content"></div>
                        }
                    </div>
                </div>
            </div>
            {menuContent === "active" ? 
                <div className="column-lectures-menu">
                    <div className="menu-header">
                        <span>Course content</span>
                        <div className="btn-close-menu" onClick={()=>setMenuStatus()}>
                            <IoIosClose size={32} className="btn-close-menu-icon"/>
                        </div>
                    </div>
                    <div className="menu-bogy">
                        <div className="section">
                            <div className="section-header" onClick={()=>sectionContentShow()}>
                                <div className="section-header-info">
                                    <span>Section title</span><br/>
                                    1/1 | 3 min
                                </div>
                                {sectionShow === "" ?
                                    <div className="section-header-icon"><FaAngleDown size={16}/></div>
                                    :
                                    <div className="section-header-icon-rotate"><FaAngleDown size={16}/></div>
                                }
                            </div>
                            {/* {sectionShow === "" ?
                                <div className="section-body"></div>
                                :
                                <div className="section-body" style={{display: "none"}}></div>
                            }  */}
                            <div className="section-body" style={sectionShow === "" ? {display: "none"} : {display: "flex"}}>
                                <div className="lecture">
                                    <div className="lecture-checkbox">
                                        <div className="_checkbox"></div>
                                    </div>
                                    <div className="lecture-info">
                                        <div className="lecture-info-title">1. Introduce to web design</div>
                                        <div className="lecture-info-more">
                                            <GoVideo />
                                            <span>3 min</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div> 
                    </div>
                </div> 
                : <></> }
        </div>
    )
}

export default CourseContent;