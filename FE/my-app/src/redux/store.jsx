import { configureStore } from "@reduxjs/toolkit"
import baseLoaderReducer from "./baseLoader.slice"

export default configureStore({
    reducer: {
        baseLoader: baseLoaderReducer
    }
})