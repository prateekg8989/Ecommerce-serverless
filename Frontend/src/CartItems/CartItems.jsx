import React, { useState, useEffect } from "react";
import { Form, Card, Col, Button } from "react-bootstrap";
import "./CartItems.css";
import { useSelector, useDispatch } from "react-redux";
import {
  getCart,
  removeItemFromCart,
  updateProductQuantity,
} from "../reducers/cartSlice";
import {
  faTrash,
  faPlusCircle,
  faMinusCircle,
} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { useHistory } from "react-router-dom";

export default function CartItems(props) {
  const history = useHistory();
  const dispatch = useDispatch();
  const userCart = useSelector((state) => state.cart);
  const user = useSelector((state) => state.users);
  const { loading } = useSelector((state) => state.cart);

  const handleMinusCartClicked = (productId) => {
    let itemFound = userCart.cartItems.find(
      (ele) => ele.productId == productId
    );
    let body = {
      userId: user.userId,
      newQuantity: itemFound.quantities - 1,
      cartItemId: productId,
    };
    dispatch(updateProductQuantity(body));
  };

  const handlePlusCartClicked = (productId) => {
    let itemFound = userCart.cartItems.find(
      (ele) => ele.productId == productId
    );
    let body = {
      userId: user.userId,
      newQuantity: itemFound.quantities + 1,
      cartItemId: productId,
    };
    dispatch(updateProductQuantity(body));
  };

  const handleRemoveItemClicked = (cartItemId) => {
    let body = {
      userId: user.userId,
      cartItemId: cartItemId,
    };
    dispatch(removeItemFromCart(body));
  };

  const handlePBuyClicked = () => {
    history.push("/select-address");
  };
  return (
    <div>
      {userCart &&
        userCart.cartItems &&
        userCart.cartItems.map((item) => (
          <div key={item.productId} className="d-flex itemContainer py-3 mb-2">
            <div className="col-md-4">
              <img
                className="image"
                src={item.imageUrls1 ? item.imageUrls1[0] : ""}
              />
            </div>
            <div className="col-md-8 d-flex">
              <div className="col-md-7">
                <p className="font-weight-bold">{item.productName}</p>
                <p className="text-muted">{item.productSummary}</p>
                <div>
                  <div className="font14 text-muted striked">
                    {" "}
                    Rs. {item.costPrice}
                  </div>
                  <div className="font16 text-primary d-flex align-items-center">
                    Rs. {item.sellingPrice} &nbsp;
                    <div className="font14 text-muted">
                      {parseInt(
                        ((item.costPrice - item.sellingPrice) * 100) /
                          item.costPrice
                      )}
                      % OFF
                    </div>
                  </div>
                </div>

                <div className="d-flex p-0 justify-content-between w-50">
                  <div>
                    <FontAwesomeIcon
                      className="cursor-pointer"
                      icon={faMinusCircle}
                      onClick={() => handleMinusCartClicked(item.productId)}
                    />
                  </div>
                  <div className="px-1">
                    {
                      userCart.cartItems.find(
                        (ele) => ele.productId == item.productId
                      ).quantities
                    }
                  </div>
                  <div>
                    <FontAwesomeIcon
                      color="blue"
                      className="cursor-pointer"
                      icon={faPlusCircle}
                      onClick={() => handlePlusCartClicked(item.productId)}
                    />
                  </div>
                </div>
              </div>
              <div className="col-md-5 d-flex justify-content-center align-items-center">
                <button
                  className="btn btn-sm btn-outline-danger"
                  onClick={() => handleRemoveItemClicked(item.productId)}
                >
                  Remove Item
                </button>
              </div>
            </div>
          </div>
        ))}
    </div>
  );
}
