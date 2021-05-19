import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import axios from "axios";

export const fetchCustomerDetails = createAsyncThunk("customer/fetchCustomerDetails", async (obj) => {
    try {
        if (sessionStorage.getItem('tokenId')) {
            let response = await axios.get(`https://aro6clc3xi.execute-api.us-east-2.amazonaws.com/dev/customer?userId=${obj}`, {
                headers: {
                    'Authorization': 'Bearer ' + sessionStorage.getItem('tokenId')
                }
            });
            console.log('customer details fetched', response);
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

export const updateCustomerDetails = createAsyncThunk("customer/updateCustomerDetails", async (obj) => {
    try {
        if (sessionStorage.getItem('tokenId')) {
            let response = await axios.put(`https://aro6clc3xi.execute-api.us-east-2.amazonaws.com/dev/customer?userId=${obj['userId']}`, obj, {
                headers: {
                    'Authorization': 'Bearer ' + sessionStorage.getItem('tokenId')
                }
            });
            console.log('customer details updated', response);
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

export const addCustomerAddress = createAsyncThunk("customer/addCustomerAddress", async (obj) => {
    try {
        if (sessionStorage.getItem('tokenId')) {

            let response = await axios.post(`https://aro6clc3xi.execute-api.us-east-2.amazonaws.com/dev/customer-address?userId=${obj['userId']}`, obj['address'], {
                headers: {
                    'Authorization': 'Bearer ' + sessionStorage.getItem('tokenId')
                }
            });
            console.log('customer address added', response);
            if (response.status == 200) {
                return { data: response.data, address: obj['address'] };
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

export const fetchAllAddresses = createAsyncThunk("customer/fetchAllAddresses", async (userId) => {
    try {
        if (sessionStorage.getItem('tokenId')) {

            let response = await axios.get(`https://aro6clc3xi.execute-api.us-east-2.amazonaws.com/dev/customer-addresses?userId=${userId}`, {
                headers: {
                    'Authorization': 'Bearer ' + sessionStorage.getItem('tokenId')
                }
            });
            console.log('all customers address fetched', response);
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



const customerSlice = createSlice({
    name: "customer",
    initialState: {
        customerDetails: {},
        customerAddresses: [],
        loading: false
    },
    reducers: {
    },
    extraReducers: {
        [fetchCustomerDetails.pending]: (state, action) => {
            state.loading = true;
        },
        [fetchCustomerDetails.fulfilled]: (state, action) => {
            state.loading = false;
            if (action.payload) {
                state.customerDetails = { ...action.payload };
            }
        },
        [fetchCustomerDetails.rejected]: (state, action) => {
            state.loading = false;
        },

        [updateCustomerDetails.pending]: (state, action) => {
            state.loading = true;
        },
        [updateCustomerDetails.fulfilled]: (state, action) => {
            state.loading = false;
        },
        [updateCustomerDetails.rejected]: (state, action) => {
            state.loading = false;
        },

        [fetchAllAddresses.pending]: (state, action) => {
            state.loading = true;
        },
        [fetchAllAddresses.fulfilled]: (state, action) => {
            state.loading = false;
            if (action.payload) {
                state.customerAddresses = [...action.payload];
            }
        },
        [fetchAllAddresses.rejected]: (state, action) => {
            state.loading = false;
        },

        [addCustomerAddress.pending]: (state, action) => {
            state.loading = true;
        },
        [addCustomerAddress.fulfilled]: (state, action) => {
            state.loading = false;
            if (action.payload) {
                state.customerAddresses = [...state.customerAddresses, action.payload.address];
            }
        },
        [addCustomerAddress.rejected]: (state, action) => {
            state.loading = false;
        },
    },
});

// export const { mainCategoryAdd, subCategoryAdd } = customerSlice.actions;

export default customerSlice.reducer;
