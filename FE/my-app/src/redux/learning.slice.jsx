import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  courses: [],
  lists: [],
  listInteraction: { status: "none" },
};

const learningSlice = createSlice({
  name: "learningLoader",
  initialState,
  reducers: {
    setCourses: (state, action) => {
      state.courses = action.payload;
    },
    setLists: (state, action) => {
      state.lists = action.payload;
    },
    setListInteraction: (state, action) => {
      state.listInteraction = action.payload;
    },
  },
  selectors: {
    getListInteraction: (state) => state.listInteraction,
    getCourses: (state) => state.courses,
    getLists: (state) => state.lists,
  },
});

export const { setListInteraction, setCourses, setLists } =
  learningSlice.actions;
export const { getListInteraction, getCourses, getLists } =
  learningSlice.selectors;

export default learningSlice.reducer;
