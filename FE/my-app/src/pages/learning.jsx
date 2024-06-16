import React, { useState } from "react";
import "../styles/learning.css"

const Learning = () => {
    const [tabSelected, setTabSelected] = useState("all-courses")
    return (
        <div className="my-learning-page">
            <div className="tab-selection-container">
                <div className="title">My learning</div>
                <div className="tab-selection-group">
                    {tabSelected === "all-courses" ?
                        (<div className="all-courses isActived">
                            <span>All courses</span>
                        </div>)
                        :
                        (<div className="all-courses" onClick={() => {setTabSelected("all-courses")}}>
                            <span>All courses</span>
                        </div>)
                    }
                    {tabSelected === "my-lists" ?
                        (<div className="my-lists isActived">
                            <span>My lists</span>
                        </div>)
                        :
                        (<div className="my-lists" onClick={() => {setTabSelected("my-lists")}}>
                            <span>My lists</span>
                        </div>)
                    }  
                </div>
            </div>
            <div className="tab-content-container">
                hihihi
            </div>
        </div>
    )
}

export default Learning;