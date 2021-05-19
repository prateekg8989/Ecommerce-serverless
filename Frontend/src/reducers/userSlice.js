import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import axios from "axios";

export const signInUser = createAsyncThunk("users/signInUser", async (body) => {
    try {
        const response = await axios.post(`https://aro6clc3xi.execute-api.us-east-2.amazonaws.com/dev/signin`, body);
        console.log('Sign In api response:- ', response)
        let obj = {
            status: 200, ...response.data
        }
        return obj;
    } catch (err) {
        console.log(err.response.data);
        let obj = {
            status: 500, ...err.response.data
        }
        return obj;
    }
});

export const signUpUser = createAsyncThunk("users/signUpUser", async (userDetails) => {
    try {
        const response = await axios.post(`https://aro6clc3xi.execute-api.us-east-2.amazonaws.com/dev/signup`, userDetails);
        console.log('Sign up api response:- ', response)
        return response.data;
    } catch (err) {
        console.log(err);
    }
});



const userSlice = createSlice({
    name: "users",
    initialState: {
        loading: false,
        username: '',
        userId: '',
        tokenId: '',
        userrole: '',
        isUserLoggedIn: false,
        isError: false,
        errorMessage: ''
    },
    reducers: {
        setUserDetailsFromSS(state, action) {
            state.username = action.payload.username;
            state.userrole = action.payload.userrole;
            state.tokenId = action.payload.tokenId;
            state.userId = action.payload.userId;
            state.isUserLoggedIn = true;
        },

        logoutTheUser(state, action) {
            state = {
                loading: false,
                username: '',
                tokenId: '',
                userrole: '',
                userId: '',
                isUserLoggedIn: false,
                isError: false,
                errorMessage: ''
            };
            sessionStorage.removeItem('username');
            sessionStorage.removeItem('userId');
            sessionStorage.removeItem('userrole');
            sessionStorage.removeItem('tokenId');
        }


    },
    extraReducers: {
        [signInUser.pending]: (state, action) => {
            state.loading = true;
        },
        [signInUser.fulfilled]: (state, action) => {
            if (action.payload.status == 200) {
                state.username = action.payload.username;
                state.userId = action.payload.userId;
                state.tokenId = action.payload.token;
                state.userrole = action.payload.userrole;
                state.isUserLoggedIn = true;
                sessionStorage.setItem('username', state.username);
                sessionStorage.setItem('userId', state.userId);
                sessionStorage.setItem('tokenId', state.tokenId);
                sessionStorage.setItem('userrole', state.userrole);
                state.loading = false;
            } else {
                state.loading = false;
                state.isError = true;
                state.errorMessage = action.payload.message
            }

        },
        [signInUser.rejected]: (state, action) => {
            state.loading = false;
        },

        [signUpUser.pending]: (state, action) => {
            state.loading = true;
        },
        [signUpUser.fulfilled]: (state, action) => {
            state.loading = false;
        },
        [signUpUser.rejected]: (state, action) => {
            state.loading = false;
        },
    },
});

export const { setUserDetailsFromSS, logoutTheUser } = userSlice.actions;

export default userSlice.reducer;
