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
    setCourses: (state, action) => action.payload,
    setCategories: (state, action) => action.payload,
    setUsers: (state, action) => action.payload,
  },
  selectors: {
    getAllCourses: (state) => state.courses,
    getAllCategories: (state) => state.categories,
    getAllUsers: (state) => state.users,
  },
});

export const { setCourses, setCategories, setUsers } =
  systemLoaderSlice.actions;
export const { getAllCourses, getAllCategories, getAllUsers } =
  systemLoaderSlice.selector;

export default systemLoaderSlice.reducer;
