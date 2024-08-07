import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  user: { userID: "1", role: "" },
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

const baseLoaderSlice = createSlice({
  name: "baseLoader",
  initialState,
  reducers: {
    setBaseLoad: (state, action) => {
      if (action.payload.key == "entire") {
        return action.payload;
      } else {
        let key = action.payload.key;
        state[key] = action.payload.value;
      }
    },
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
