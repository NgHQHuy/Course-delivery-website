import { configureStore } from "@reduxjs/toolkit";
import baseLoaderSlice from "./baseLoader.slice";
import learningSlice from "./learning.slice";

export default configureStore({
  reducer: {
    baseLoader: baseLoaderSlice,
    learningLoader: learningSlice,
  },
});
