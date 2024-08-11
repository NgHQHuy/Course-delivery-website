import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  data: [],
  listInteraction: { status: "none", courseID: "" },
};

const learningSlice = createSlice({
  name: "learningLoader",
  initialState,
  reducers: {
    setListInteraction: (state, action) => {
      state.listInteraction = action.payload;
    },
  },
  selectors: {
    getListInteraction: (state) => state.listInteraction,
  },
});

export const { setListInteraction } = learningSlice.actions;
export const { getListInteraction } = learningSlice.selectors;

export default learningSlice.reducer;
