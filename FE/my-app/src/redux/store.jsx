import { configureStore } from "@reduxjs/toolkit";
import baseLoaderSlice from "./baseLoader.slice";
import learningSlice from "./learning.slice";
import systemLoaderSlice from "./systemLoader.slice";
import coursesLoaderSlice from "./coursesLoader.slice";

export default configureStore({
  reducer: {
    baseLoader: baseLoaderSlice,
    learningLoader: learningSlice,
    systemLoader: systemLoaderSlice,
    coursesLoader: coursesLoaderSlice,
  },
});
