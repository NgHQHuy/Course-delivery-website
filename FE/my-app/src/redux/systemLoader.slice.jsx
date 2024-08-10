import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  courses: [],
  categories: [],
  users: [],
};

const systemLoaderSlice = createSlice({
  name: "systemLoader",
  initialState,
  reducers: {
    setAllCourses: (state, action) => {
      state.courses = action.payload;
    },
    setAllCategories: (state, action) => {
      state.categories = action.payload;
    },
    setAllUsers: (state, action) => {
      state.users = action.payload;
    },
  },
  selectors: {
    getAllCourses: (state) => state.courses,
    getAllCategories: (state) => state.categories,
    getAllUsers: (state) => state.users,
  },
});

export const { setAllCourses, setAllCategories, setAllUsers } =
  systemLoaderSlice.actions;
export const { getAllCourses, getAllCategories, getAllUsers } =
  systemLoaderSlice.selectors;

export default systemLoaderSlice.reducer;
