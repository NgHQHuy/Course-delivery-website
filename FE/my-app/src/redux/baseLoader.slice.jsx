import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  user: { userID: null, role: "", username: "" },
  profile: {},
  learning: {
    courses: [],
    lists: [],
  },
  cart: {
    cartID: null,
    cartItems: [],
  },
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
        learning: {
          courses: [],
          lists: [],
        },
        cart: {
          cartID: "",
          cartItems: [],
        },
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
