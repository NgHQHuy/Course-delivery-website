import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  user: {},
  profile: {},
  learning: {
    courses: [],
    lists: [],
    list_courses: [],
  },
  cart: [],
};

const baseLoaderSlice = createSlice({
  name: "baseLoader",
  initialState,
  reducers: {
    setBaseLoad: (state) => {},
  },
  selectors: {
    getBaseLoad: (state) => state.value,
  },
});

export const { setBaseLoad } = baseLoaderSlice.actions;
export const { getBaseLoad } = baseLoaderSlice.selectors;

export default baseLoaderSlice.reducer;
