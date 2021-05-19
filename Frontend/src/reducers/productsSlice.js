import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import axios from "axios";
export const fetchProducts = createAsyncThunk("products/fetchProducts", async () => {
    try {
        const response = await fetch(`https://aro6clc3xi.execute-api.us-east-2.amazonaws.com/dev/products`);
        const products = await response.json();
        console.log(products)
        return products;
    } catch (err) {
        console.log(err);
    }
});

export const fetchProductsByCategoryId = createAsyncThunk("products/fetchProductsByCategoryId", async (categoryId) => {
    try {
        const response = await fetch(`https://aro6clc3xi.execute-api.us-east-2.amazonaws.com/dev/products?mainCategoryId=${categoryId}`);
        const products = await response.json();
        console.log(products)
        return products;
    } catch (err) {
        console.log(err);
    }
});

export const fetchProductById = createAsyncThunk("products/fetchProductById", async (productId) => {
    try {
        const response = await fetch(`https://aro6clc3xi.execute-api.us-east-2.amazonaws.com/dev/product?productId=${productId}`);
        const product = await response.json();
        console.log(product)
        return product;
    } catch (err) {
        console.log(err);
    }
});


export const fetchProductsBySubCategoryId = createAsyncThunk("products/fetchProductsBySubCategoryId", async (subcategoryId) => {
    try {
        const response = await fetch(`https://aro6clc3xi.execute-api.us-east-2.amazonaws.com/dev/products?subCategoryId=${subcategoryId}`);
        const products = await response.json();
        console.log(products)
        return products;
    } catch (err) {
        console.log(err);
    }
});

export const productAdd = createAsyncThunk("products/productAdd", async (obj) => {
    try {
        if (sessionStorage.getItem('tokenId')) {
            let response = await axios.post(`https://aro6clc3xi.execute-api.us-east-2.amazonaws.com/dev/product`, obj, {
                headers: {
                    'Authorization': 'Bearer ' + sessionStorage.getItem('tokenId')
                }
            });
            console.log('main category added', response);
            alert('Product added');
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

export const productEdit = createAsyncThunk("products/productEdit", async (obj) => {
    try {
        if (sessionStorage.getItem('tokenId')) {
            let response = await axios.put(`https://aro6clc3xi.execute-api.us-east-2.amazonaws.com/dev/product`, obj, {
                headers: {
                    'Authorization': 'Bearer ' + sessionStorage.getItem('tokenId')
                }
            });
            console.log('main category Edited', response);
            alert('Product Edited');
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

export const fetchS3PresignedUrl = createAsyncThunk("products/fetchS3PresignedUrl", async (obj) => {
    try {
        if (sessionStorage.getItem('tokenId')) {
            let response = await axios.get(`https://aro6clc3xi.execute-api.us-east-2.amazonaws.com/dev/s3-url?name=${obj}`, {
                headers: {
                    'Authorization': 'Bearer ' + sessionStorage.getItem('tokenId')
                }
            });
            console.log('s3 url fetched', response);
            // alert('s3 url fetched');
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

export const deleteProductById = createAsyncThunk("products/deleteProductById", async (productId) => {
    try {
        if (sessionStorage.getItem('tokenId')) {
            let response = await axios.delete(`https://aro6clc3xi.execute-api.us-east-2.amazonaws.com/dev/product?productId=${productId}`, {
                headers: {
                    'Authorization': 'Bearer ' + sessionStorage.getItem('tokenId')
                }
            });
            console.log('delete product by id', response);
            alert('Product deleted');
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


export const uploadImage = createAsyncThunk("products/uploadImage", async (obj) => {
    try {
        if (sessionStorage.getItem('tokenId')) {
            let response = await axios.put(`${obj['url']}`, obj['file']);
            console.log('uploadImage', response);
            alert('Image Uploaded');
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

export const fetchSearchProducts = createAsyncThunk("products/fetchSearchProducts", async (obj) => {
    try {
        let response = await axios.post(`https://aro6clc3xi.execute-api.us-east-2.amazonaws.com/dev/product-search`, obj);
        console.log('searching completed', response);
        return response.data;
    } catch (err) {
        console.log(err);
    }
});



const productsSlice = createSlice({
    name: "products",
    initialState: {
        list: [],
        loading: false,
        isSearched: false,
        productById: {},
        searchedEntities: [],
        productByCategoryId: [],
        productBySubCategoryId: [],
        s3Url: ""
    },
    reducers: {
        searchedCancelled(state, action) {
            state.isSearched = false;
        }
    },
    extraReducers: {
        [fetchProducts.pending]: (state, action) => {
            state.loading = true;
            state.isSearched = false;
        },
        [fetchProducts.fulfilled]: (state, action) => {
            state.loading = false;
            console.log(action.payload);
            if (action.payload) {
                state.list = [...action.payload];
            }
            state.isSearched = false;
            console.log('list::- ', state.list);
        },
        [fetchProducts.rejected]: (state, action) => {
            state.loading = false;
            state.isSearched = false;
        },

        [fetchProductById.pending]: (state, action) => {
            state.loading = true;
            state.isSearched = false;
        },
        [fetchProductById.fulfilled]: (state, action) => {
            state.loading = false;
            console.log(action.payload);
            if (action.payload) {
                state.productById = { ...action.payload };
            }
            state.isSearched = false;
            console.log('fetchProductById::- ', state.productById);
        },
        [fetchProductById.rejected]: (state, action) => {
            state.loading = false;
            state.isSearched = false;
        },

        [deleteProductById.pending]: (state, action) => {
            state.loading = true;
            state.isSearched = false;
        },
        [deleteProductById.fulfilled]: (state, action) => {
            state.loading = false;
            console.log(action.payload);
            state.isSearched = false;
            console.log('deleteProductById::- ', state.productById);
        },
        [deleteProductById.rejected]: (state, action) => {
            state.loading = false;
            state.isSearched = false;
        },


        [fetchProductsByCategoryId.pending]: (state, action) => {
            state.loading = true;
            state.isSearched = false;
        },
        [fetchProductsByCategoryId.fulfilled]: (state, action) => {
            state.loading = false;
            console.log(action.payload);
            if (action.payload) {
                state.productByCategoryId = [...action.payload];
            }
            state.isSearched = false;
            console.log('productByCategoryId::- ', state.productByCategoryId);
        },
        [fetchProductsByCategoryId.rejected]: (state, action) => {
            state.loading = false;
            state.isSearched = false;
        },

        [fetchProductsBySubCategoryId.pending]: (state, action) => {
            state.loading = true;
            state.isSearched = false;
        },
        [fetchProductsBySubCategoryId.fulfilled]: (state, action) => {
            state.loading = false;
            console.log(action.payload);
            if (action.payload) {
                state.productBySubCategoryId = [...action.payload];
            }
            state.isSearched = false;
            console.log('productBySubCategoryId::- ', state.productBySubCategoryId);
        },
        [fetchProductsBySubCategoryId.rejected]: (state, action) => {
            state.loading = false;
            state.isSearched = false;
        },

        [productAdd.pending]: (state, action) => {
            state.loading = true;
            state.isSearched = false;
        },
        [productAdd.fulfilled]: (state, action) => {
            state.loading = false;
            state.isSearched = false;
        },
        [productAdd.rejected]: (state, action) => {
            state.loading = false;
            state.isSearched = false;
        },


        [productEdit.pending]: (state, action) => {
            state.loading = true;
            state.isSearched = false;
        },
        [productEdit.fulfilled]: (state, action) => {
            state.loading = false;
            state.isSearched = false;
        },
        [productEdit.rejected]: (state, action) => {
            state.loading = false;
            state.isSearched = false;
        },
        [fetchS3PresignedUrl.pending]: (state, action) => {
            state.loading = true;
            state.isSearched = false;
        },
        [fetchS3PresignedUrl.fulfilled]: (state, action) => {
            state.loading = false;
            state.s3Url = action.payload;
            state.isSearched = false;
        },
        [fetchS3PresignedUrl.rejected]: (state, action) => {
            state.loading = false;
            state.isSearched = false;
        },

        [uploadImage.pending]: (state, action) => {
            state.loading = true;
            state.isSearched = false;
        },
        [uploadImage.fulfilled]: (state, action) => {
            state.loading = false;
            console.log('Image uploaded', action.payload)
            state.isSearched = false;
        },
        [uploadImage.rejected]: (state, action) => {
            state.loading = false;
            state.isSearched = false;
        },
        [fetchSearchProducts.pending]: (state, action) => {
            state.loading = true;
            state.isSearched = false;
        },
        [fetchSearchProducts.fulfilled]: (state, action) => {
            state.loading = false;
            state.isSearched = true;
            console.log(action.payload);
            state.searchedEntities = [...action.payload];
        },
        [fetchSearchProducts.rejected]: (state, action) => {
            state.loading = false;
            state.isSearched = false;
        },


    },
});

export const { searchedCancelled } = productsSlice.actions;

export default productsSlice.reducer;
