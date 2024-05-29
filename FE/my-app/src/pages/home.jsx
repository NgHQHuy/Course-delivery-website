import React from "react";
import { useNavigate } from "react-router-dom";
import "../styles/home.css"

const HomePage = () => {
    const navigate = useNavigate()
    const handleClick = (id) => {
        navigate(`/courses/${id}`)
    }
    return (
        <div className="home-page">
            <div className="billboard">
                <div className="billboard-text">
                    <span className="title">Come learn with us!</span><br/>
                    <span className="subtitle">Constantly cultivate knowledge and change lives - including your own</span>
                </div>
            </div>
            <div className="h-line" style={{width: "60%",height:"1px",backgroundColor: "gray"}}></div>
            <div className="popular-courses">
                <div className="popular-courses-title">Popular</div>
                <div className="popular-courses-group">
                    <div className="card-course" onClick={() => handleClick(1)}>
                        <div className="card-course-thumnail"></div>
                        <div className="card-course-title">web development</div>
                        <div className="card-course-author">tdtu</div>
                        <div className="card-course-rating">4.5</div>
                        <div className="card-course-price">100,000</div>
                    </div>
                    <div className="card-course">
                        <div className="card-course-thumnail"></div>
                        <div className="card-course-title">web development</div>
                        <div className="card-course-author">tdtu</div>
                        <div className="card-course-rating">4.5</div>
                        <div className="card-course-price">100,000</div>
                    </div>
                    <div className="card-course">
                        <div className="card-course-thumnail"></div>
                        <div className="card-course-title">web development</div>
                        <div className="card-course-author">tdtu</div>
                        <div className="card-course-rating">4.5</div>
                        <div className="card-course-price">100,000</div>
                    </div>
                    <div className="card-course">
                        <div className="card-course-thumnail"></div>
                        <div className="card-course-title">web development</div>
                        <div className="card-course-author">tdtu</div>
                        <div className="card-course-rating">4.5</div>
                        <div className="card-course-price">100,000</div>
                    </div>
                    <div className="card-course">
                        <div className="card-course-thumnail"></div>
                        <div className="card-course-title">web development</div>
                        <div className="card-course-author">tdtu</div>
                        <div className="card-course-rating">4.5</div>
                        <div className="card-course-price">100,000</div>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default HomePage;