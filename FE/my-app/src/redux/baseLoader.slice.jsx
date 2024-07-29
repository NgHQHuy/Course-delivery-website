import { createSlice } from "@reduxjs/toolkit"

const initialState = {
    user: {},
    courses: [],
    lists: [],
    list_courses: []
}

const baseLoaderSlice = createSlice({
    name: 'baseLoader',
    initialState,
    reducers: {
        setBaseLoad: state => {
        }
    },
    selectors: {
        getBaseLoad: state => state.value,
    }
})

export const { setBaseLoad } = baseLoaderSlice.actions
export const { getBaseLoad } = baseLoaderSlice.selectors

export default baseLoaderSlice.reducer