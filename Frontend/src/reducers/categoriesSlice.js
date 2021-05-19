import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import axios from "axios";
export const fetchCategories = createAsyncThunk("categories/fetchCategories", async () => {
    try {
        // const response = await fetch(`https://aro6clc3xi.execute-api.us-east-2.amazonaws.com/dev/category`,{
        //     headers: {
        //         'Access-Control-Request-Method': 'GET',
        //         'Access-Control-Request-Headers': 'Content-Type, Authorization'            }
        // });
        // const categories = await response.json();

        const response = await axios.get(`https://aro6clc3xi.execute-api.us-east-2.amazonaws.com/dev/category`);
        const categories = await response.data;

        console.log(categories);
        return categories;
    } catch (err) {
        console.log(err);
    }
});

export const mainCategoryAdd = createAsyncThunk("categories/mainCategoryAdd", async (obj) => {
    try {
        if (sessionStorage.getItem('tokenId')) {
            let response = await axios.post(`https://aro6clc3xi.execute-api.us-east-2.amazonaws.com/dev/category`, obj, {
                headers: {
                    'Authorization': 'Bearer ' + sessionStorage.getItem('tokenId')
                }
            });
            console.log('main category added', response);
            if (response.status == 200) {
                return response.data;
            } else {
                alert('Error occured');
                throw new Error("");
            }

        } else {
            alert('Please login first!!')
            throw new Error("");
        }
    } catch (err) {
        console.log(err);
        throw new Error("");
    }
});

export const subCategoryAdd = createAsyncThunk("categories/subCategoryAdd", async (obj) => {
    try {
        if (sessionStorage.getItem('tokenId')) {
            let body = {
                subCategoryName: obj['subCategoryName'],
                subCategoryDesc: obj['subCategoryDesc']
            };
            let response = await axios.post(`https://aro6clc3xi.execute-api.us-east-2.amazonaws.com/dev/sub-category?mainCategoryId=${obj['mainCategoryId']}`, body, {
                headers: {
                    'Authorization': 'Bearer ' + sessionStorage.getItem('tokenId')
                }
            });
            console.log('sub category added', response);
            if (response.status == 200) {
                return response.data;
            } else {
                alert('Error occured');
                throw new Error("");
            }

        } else {
            alert('Please login first!!')
            throw new Error("");
        }
    } catch (err) {
        console.log(err);
    }
});



const categoriesSlice = createSlice({
    name: "categories",
    initialState: {
        entities: [],
        loading: false
    },
    reducers: {
    },
    extraReducers: {
        [fetchCategories.pending]: (state, action) => {
            state.loading = true;
        },
        [fetchCategories.fulfilled]: (state, action) => {
            state.loading = false;
            console.log(action.payload);
            if (action.payload) {
                state.entities = [...action.payload];
            }
            console.log('entities::- ', state.entities);
        },
        [fetchCategories.rejected]: (state, action) => {
            state.loading = false;
        },

        [mainCategoryAdd.pending]: (state, action) => {
            state.loading = true;
        },
        [mainCategoryAdd.fulfilled]: (state, action) => {
            state.loading = false;
        },
        [mainCategoryAdd.rejected]: (state, action) => {
            state.loading = false;
        },

        [subCategoryAdd.pending]: (state, action) => {
            state.loading = true;
        },
        [subCategoryAdd.fulfilled]: (state, action) => {
            state.loading = false;
        },
        [subCategoryAdd.rejected]: (state, action) => {
            state.loading = false;
        },
    },
});

// export const { mainCategoryAdd, subCategoryAdd } = categoriesSlice.actions;

export default categoriesSlice.reducer;
