import React from "react";
import "../styles/course_info.css"
import { BsHeart } from "react-icons/bs";

const CourseInfo = () => {
    return (
        <div className="course-info">
            <div className="course-info-main">
                <div className="introduce">
                    <div className="title">Fundamentals of Cisco CLI Switch Configuration</div>
                    <div className="summary">learn about web development!</div>
                    <div className="rating">4.5</div>
                    <div className="author">Created by ABC</div>
                    <div className="update-time">Last updated: 13/05/2024</div>
                </div>
                <div className="content">
                    <div className="content-header">Course content</div>
                    <div className="content-list"></div>
                </div>
                <div className="description">
                    <div className="description-header">Description</div>
                </div>
                <div className="instructor">
                    <div className="instructor-header">Instructor</div>
                </div>
                <div className="more-by-instructor">
                    <div className="more-header">More courses by ABC</div>
                </div>
            </div>
            <div className="course-info-bar">
                <div className="course-info-card">
                    <div className="card-preview"></div>
                    <div className="card-content">
                        <div className="price">100,000</div>
                        <div className="button-group">
                            <div className="button-group-layer1">
                                <div className="add-to-cart">
                                    <button type="button">Add to cart</button>
                                </div>
                                <div className="favourite">
                                    <button type="button" className="favourite"><BsHeart size={24}/></button>
                                </div>
                            </div>
                            <div className="button-group-layer2">
                                <button type="button">Buy now</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default CourseInfo;