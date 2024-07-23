import React, { useState } from "react";
import "../styles/manager_tools.css";

const ManagerTools = () =>{
    const [toolSeleted, setToolSelected] = useState("course")
    const toolClick = (tool) => {
        setToolSelected(tool)
    }
    return (
        <div className="manager-page">
            <div className="tools-group">
                <div className={toolSeleted == "course" ? "course-management isActived" : "course-management"} onClick={()=>toolClick("course")}>Course</div>
                <div className={toolSeleted == "team" ? "team isActived" : "team"} onClick={()=>toolClick("team")}>Team</div>
            </div>
            <div className="tool-panel"></div>
        </div>
    )
}

export default ManagerTools;