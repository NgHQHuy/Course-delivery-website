import React from "react";
import "../styles/course_card_enroll.css";
import { SlOptionsVertical, SlPlus } from "react-icons/sl";
import { RiShareForwardFill, RiMenuAddFill } from "react-icons/ri";
import { MdOutlineRemoveCircleOutline } from "react-icons/md";


const CourseCardEnroll = (props) => {
    const percent = 75
    console.log(props.pageView);
    return (
        <div className="course-card">
            <div className="course-options">
                <SlOptionsVertical size={16} className="options-icon"/>
                <div className="options-dropdown">
                    {props.pageView === "all-courses" ?
                        <>
                        <div className="share">
                            <RiShareForwardFill />
                            <span>Share</span>
                        </div>
                        <div className="create-new-list">
                            <SlPlus />
                            <span>Create new list</span>
                        </div>
                        <div className="add-to-list">
                            <RiMenuAddFill />
                            <span>Add to existing list</span>
                        </div>
                        </>
                        :
                        <div className="remove-course-from-list">
                            <MdOutlineRemoveCircleOutline />
                            <span>Remove course</span>
                        </div>
                    }
                    
                </div>    
            </div>
            <div className="course-thumnail">
            </div>
            <div className="course-title">course title here here here here  here here here here here hhhhereeeeeeeeee</div>
            <div className="course-author">author name</div>
            <div className="course-progression">
                <div className="course-progression-bar">
                    <div className="bar-percent" style={{width: `${percent}%`}}></div>
                </div>
                <div className="course-progression-text">{percent}%</div>
            </div>
        </div>
    )
}

export default CourseCardEnroll;