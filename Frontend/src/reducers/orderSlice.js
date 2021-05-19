import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import axios from "axios";


export const placeOrder = createAsyncThunk("order/placeOrder", async (obj) => {
    try {
        if (sessionStorage.getItem('tokenId')) {
            let response = await axios.post(`https://aro6clc3xi.execute-api.us-east-2.amazonaws.com/dev/order?userId=${obj['userId']}&addressId=${obj['addressId']}`, {}, {
                headers: {
                    'Authorization': 'Bearer ' + sessionStorage.getItem('tokenId')
                }
            });
            if (response.status == 200 ) {
                console.log('order placed', response);
                return { data: response.data, status: response.status };
            } else if (response.status == 202) {
                console.log('Inventory Issue', response);
                return { data: response.data, status: response.status };
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


export const fetchOrders = createAsyncThunk("order/fetchOrders", async (obj) => {
    try {
        if (sessionStorage.getItem('tokenId')) {
            let response = await axios.get(`https://aro6clc3xi.execute-api.us-east-2.amazonaws.com/dev/orders?userId=${obj}`, {
                headers: {
                    'Authorization': 'Bearer ' + sessionStorage.getItem('tokenId')
                }
            });
            if (response.status == 200 ) {
                console.log('orders fetched', response);
                return { data: response.data, status: response.status };
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


const orderSlice = createSlice({
    name: "order",
    initialState: {
        loading: false,
        insufficientItems: [],
        orders:[]
    },
    reducers: {
    },
    extraReducers: {
        [placeOrder.pending]: (state, action) => {
            state.loading = true;
        },
        [placeOrder.fulfilled]: (state, action) => {
            state.loading = false;
            if (action.payload && action.payload.status == 200) {
                state.isOrderPlaced = true;
            } else if (action.payload && action.payload.status == 202) {
                console.log('error while placing order', action.payload.data);
            }
        },
        [placeOrder.rejected]: (state, action) => {
            state.loading = false;
        },

        [fetchOrders.pending]: (state, action) => {
            state.loading = true;
        },
        [fetchOrders.fulfilled]: (state, action) => {
            state.loading = false;
            if (action.payload) {
                state.orders = [...action.payload.data];
            }
        },
        [fetchOrders.rejected]: (state, action) => {
            state.loading = false;
        },

    },
});

// export const { mainCategoryAdd, subCategoryAdd } = orderSlice.actions;

export default orderSlice.reducer;
