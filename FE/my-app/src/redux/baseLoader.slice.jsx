import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  user: { userID: null, role: "", username: "" },
  profile: {},
};

const baseLoaderSlice = createSlice({
  name: "baseLoader",
  initialState,
  reducers: {
    setBaseLoad: (state, action) => action.payload,
    cleanBaseLoad: (state) => {
      const base = {
        user: {},
        profile: {},
      };

      return base;
    },
  },
  selectors: {
    getBaseLoad: (state) => state,
  },
});

export const { setBaseLoad, cleanBaseLoad } = baseLoaderSlice.actions;
export const { getBaseLoad } = baseLoaderSlice.selectors;

export default baseLoaderSlice.reducer;
