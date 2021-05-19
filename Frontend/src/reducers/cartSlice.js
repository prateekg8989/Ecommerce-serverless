import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import axios from "axios";


export const getCart = createAsyncThunk("cart/getCart", async (obj) => {
    try {
        if (sessionStorage.getItem('tokenId')) {
            let response = await axios.get(`https://aro6clc3xi.execute-api.us-east-2.amazonaws.com/dev/cart?userId=${obj['userId']}`, {
                headers: {
                    'Authorization': 'Bearer ' + sessionStorage.getItem('tokenId')
                }
            });
            console.log('fetched cart item', response);
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



export const addItemToCart = createAsyncThunk("cart/addItemToCart", async (obj) => {
    try {
        if (sessionStorage.getItem('tokenId')) {
            // alert(JSON.stringify(obj['item']))
            let response = await axios.post(`https://aro6clc3xi.execute-api.us-east-2.amazonaws.com/dev/cart-item?userId=${obj['userId']}`, obj['item'], {
                headers: {
                    'Authorization': 'Bearer ' + sessionStorage.getItem('tokenId')
                }
            });
            console.log('item added to cart', response);
            if (response.status == 200) {
                return { data: response.data, item: obj['item'] };
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

export const updateProductQuantity = createAsyncThunk("cart/updateProductQuantity", async (obj) => {
    try {
        if (sessionStorage.getItem('tokenId')) {
            let userId = obj['userId'];
            let newQuantity = obj['newQuantity'];
            let cartItemId = obj['cartItemId'];
            let response = await axios.put(`https://aro6clc3xi.execute-api.us-east-2.amazonaws.com/dev/cart-item?userId=${userId}&newQuantity=${newQuantity}&cartItemId=${cartItemId}`, obj, {
                headers: {
                    'Authorization': 'Bearer ' + sessionStorage.getItem('tokenId')
                }
            });
            console.log('updated the quantity of product in cart', response);
            if (response.status == 200) {
                return { data: response.data, newQuantity, cartItemId };
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


export const removeItemFromCart = createAsyncThunk("cart/removeItemFromCart", async (obj) => {
    try {
        if (sessionStorage.getItem('tokenId')) {
            let userId = obj['userId'];
            let cartItemId = obj['cartItemId'];
            let response = await axios.delete(`https://aro6clc3xi.execute-api.us-east-2.amazonaws.com/dev/cart-item?userId=${userId}&cartItemId=${cartItemId}`, {
                headers: {
                    'Authorization': 'Bearer ' + sessionStorage.getItem('tokenId')
                }
            });
            console.log('removed the item from cart', response);
            if (response.status == 200) {
                return { data: response.data, cartItemId };
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



const cartSlice = createSlice({
    name: "cart",
    initialState: {
        cartItems: [],
        loading: false
    },
    reducers: {
    },
    extraReducers: {
        [getCart.pending]: (state, action) => {
            state.loading = true;
        },
        [getCart.fulfilled]: (state, action) => {
            state.loading = false;
            if (action.payload) {
                state.cartItems = [...action.payload.cartItems]
            }
        },
        [getCart.rejected]: (state, action) => {
            state.loading = false;
        },

        [addItemToCart.pending]: (state, action) => {
            state.loading = true;
        },
        [addItemToCart.fulfilled]: (state, action) => {
            state.loading = false;
            if (action.payload) {
                state.cartItems = [...state.cartItems, action.payload.item];
            }
        },
        [addItemToCart.rejected]: (state, action) => {
            state.loading = false;
        },

        [updateProductQuantity.pending]: (state, action) => {
            state.loading = true;
        },
        [updateProductQuantity.fulfilled]: (state, action) => {
            state.loading = false;
            if (action.payload) {
                let newQuantity = action.payload.newQuantity;
                let cartItemId = action.payload.cartItemId;
                if (newQuantity == 0) {
                    let index = state.cartItems.findIndex(item => item.productId == cartItemId);
                    if (index != -1) {
                        state.cartItems.splice(index, 1);
                        state.cartItems = [...state.cartItems];
                    }
                } else {
                    let item = state.cartItems.find(item => item.productId == cartItemId);
                    item.quantities = newQuantity;
                }
            }
        },
        [updateProductQuantity.rejected]: (state, action) => {
            state.loading = false;
        },

        [removeItemFromCart.pending]: (state, action) => {
            state.loading = true;
        },
        [removeItemFromCart.fulfilled]: (state, action) => {
            state.loading = false;
            if (action.payload) {
                let cartItemId = action.payload.cartItemId;
                let index = state.cartItems.findIndex(item => item.productId == cartItemId);
                if (index != -1) {
                    state.cartItems.splice(index, 1);
                    state.cartItems = [...state.cartItems];
                }
            }
        },
        [removeItemFromCart.rejected]: (state, action) => {
            state.loading = false;
        }


    },
});

// export const { mainCategoryAdd, subCategoryAdd } = cartSlice.actions;

export default cartSlice.reducer;
