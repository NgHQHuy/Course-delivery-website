import React, { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import "../styles/profile.css";
import d_avatar from "../assets/default_avatar.jpg";

import { getBaseLoad } from "../redux/baseLoader.slice";

const Profile = () => {
  const baseLoader = useSelector(getBaseLoad);
  const [pageType, setPageType] = useState("view");
  const [profile, setProfile] = useState(baseLoader.profile);
  useEffect(() => {}, []);
  const avatarChange = () => {};
  const genderChange = (e) => {
    setProfile({ ...profile, gender: e.target.value });
  };
  const editBtnClicked = () => {
    if (pageType === "view") {
      setPageType("edit");
    } else {
    }
  };
  return (
    <div className="profile-container">
      <div className="profile-avatar">
        <img
          src={baseLoader.profile.avatar ? baseLoader.profile.avatar : d_avatar}
          alt="profile-avatar"
          style={{
            width: "100%",
            height: "100%",
            objectFit: "cover",
          }}
        />
      </div>
      <div style={pageType === "View" ? { display: "none" } : {}}>
        <input type="file" onChange={() => avatarChange()} />
      </div>
      <div className="profile-name">
        <span>Name:</span>
        {pageType === "view" ? (
          <span>
            {baseLoader.profile.name ? baseLoader.profile.name : "unknow"}
          </span>
        ) : (
          <input
            type="text"
            placeholder={profile.name ? profile.name : "Empty"}
          />
        )}
      </div>
      <div className="profile-gender">
        <span>Gender:</span>
        {pageType === "view" ? (
          <span>
            {baseLoader.profile.gender ? baseLoader.profile.gender : "unknow"}
          </span>
        ) : (
          <select
            value={profile.gender ? profile.gender : "UNSPECIFIED"}
            onChange={(e) => genderChange(e)}
          >
            <option value="MALE">Male</option>
            <option value="FEMALE">Female</option>
            <option value="UNSPECIFIED">Unspecified</option>
          </select>
        )}
      </div>
      <div className="profile-age">
        <span>Age:</span>
        {pageType === "view" ? (
          <span>
            {baseLoader.profile.age ? baseLoader.profile.age : "unknow"}
          </span>
        ) : (
          <input
            type="text"
            placeholder={profile.age ? profile.age : "Empty"}
          />
        )}
      </div>
      <div className="profile-phone">
        <span>Phone:</span>
        {pageType === "view" ? (
          <span>
            {baseLoader.profile.phone ? baseLoader.profile.phone : "unknow"}
          </span>
        ) : (
          <input
            type="text"
            placeholder={profile.phone ? profile.phone : "Empty"}
          />
        )}
      </div>
      <button
        style={
          pageType === "view" ? { display: "none" } : { marginRight: "10px" }
        }
        onClick={() => {
          setPageType("view");
          setProfile(baseLoader);
        }}
      >
        Back
      </button>
      <button className="profile-btn-edit" onClick={() => editBtnClicked()}>
        {pageType === "view" ? "Edit" : "Save"}
      </button>
    </div>
  );
};

export default Profile;
