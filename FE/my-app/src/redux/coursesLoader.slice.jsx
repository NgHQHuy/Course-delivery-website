import { createSlice } from "@reduxjs/toolkit";

const initialState = [];

const coursesLoader = createSlice({
  name: "coursesLoader",
  initialState,
  reducers: {
    setListAllCourses: (state, action) => action.payload,
  },
  selectors: { getListAllCourses: (state) => state },
});
export const { setListAllCourses } = coursesLoader.actions;
export const { getListAllCourses } = coursesLoader.selectors;
export default coursesLoader.reducer;
