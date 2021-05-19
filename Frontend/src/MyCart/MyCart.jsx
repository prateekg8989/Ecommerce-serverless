import React, { useState, useEffect } from "react";
import { Form, Card, Col, Button } from "react-bootstrap";
import "./MyCart.css";
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
import CartItems from "../CartItems/CartItems";
import CartAmountSection from "../CartAmountSection/CartAmountSection";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { useHistory } from "react-router-dom";

export default function MyCart() {
  const history = useHistory();
  const dispatch = useDispatch();
  const userCart = useSelector((state) => state.cart);
  const user = useSelector((state) => state.users);
  const { loading } = useSelector((state) => state.cart);


  const handlePBuyClicked = () => {
    history.push("/select-address");
  };
  return (
    <div className="mt-5">
      {!loading ? (
        <>
          <h4 className="mb-4">
            My Cart{" "}
            {userCart && userCart.cartItems && userCart.cartItems.length ? (
              <>, ({userCart.cartItems.length})</>
            ) : (
              ""
            )}
          </h4>
          <div className="col-md-12 d-flex">
            <div className="col-md-7">
              <CartItems />
            </div>
            <div className="col-md-5">
              <CartAmountSection />
              <div className="text-center">
                <button
                  className="btn btn-md btn-outline-primary w-75"
                  onClick={() => handlePBuyClicked()}
                >
                  Proceed to buy
                </button>
              </div>
            </div>
          </div>
        </>
      ) : (
        "Loading...."
      )}
    </div>
  );
}
