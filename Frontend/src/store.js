import { configureStore } from "@reduxjs/toolkit";
import categoriesReducer from "./reducers/categoriesSlice";
import customerReducer from "./reducers/customerSlice";
import cartReducer from "./reducers/cartSlice";
import productsSlice from "./reducers/productsSlice";
import userReducer from "./reducers/userSlice";
import orderReducer from "./reducers/orderSlice";

export default configureStore({
  reducer: {
    categories: categoriesReducer,
    customer: customerReducer,
    cart: cartReducer,
    products: productsSlice,
    users: userReducer,
    order: orderReducer
  },
});
