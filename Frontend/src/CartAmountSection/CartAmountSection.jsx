import React, { useState, useEffect } from "react";
import { Form, Card, Col, Button } from "react-bootstrap";
import "./CartAmountSection.css";
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
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { useHistory } from "react-router-dom";

export default function CartAmountSection() {
  const history = useHistory();
  const userCart = useSelector((state) => state.cart);
  const { loading } = useSelector((state) => state.cart);
  const [totalCostPrice, setTotalCostPrice] = useState(-1);
  const [totalSellingPrice, setTotalSellingPrice] = useState(-1);
  const [totalDiscount, setTotalDiscount] = useState(-1);

  useEffect(() => {
    if (
      !loading &&
      userCart &&
      userCart.cartItems &&
      userCart.cartItems.length > 0
    ) {
      let costPriceTempTotal = 0;
      let sellingPriceTempTotal = 0;
      let discountTempTotal = 0;
      userCart.cartItems.map((item) => {
        costPriceTempTotal =
          costPriceTempTotal + item.costPrice * item.quantities;
        sellingPriceTempTotal =
          sellingPriceTempTotal + item.sellingPrice * item.quantities;
      });
      discountTempTotal =
        discountTempTotal + (costPriceTempTotal - sellingPriceTempTotal);
      setTotalCostPrice(costPriceTempTotal);
      setTotalSellingPrice(sellingPriceTempTotal);
      setTotalDiscount(discountTempTotal);
    }
  }, [loading, userCart]);


  return (
    <div>
      {userCart && userCart.cartItems && userCart.cartItems.length > 0 && (
        <>
          <h4 className="text-muted">Price Details</h4>
          <hr />
          <div className="col-md-12 d-flex mb-3">
            <div className="col-md-7 leftHeadingPriceSection">
              Price{" "}
              {userCart.cartItems.length ? (
                <> ({userCart.cartItems.length} items)</>
              ) : (
                ""
              )}
            </div>
            <div className="col-md-5 rightHeadingPriceSection">
              Rs. {totalCostPrice != -1 ? totalCostPrice : ""}
            </div>
          </div>
          <div className="col-md-12 d-flex mb-3">
            <div className="col-md-7 leftHeadingPriceSection">Discount</div>
            <div className="col-md-5 rightHeadingPriceSection text-success">
              - Rs. {totalDiscount != -1 ? totalDiscount : ""}
            </div>
          </div>
          <div className="col-md-12 d-flex py-3 totalAmountContainer font-weight-bold">
            <div className="col-md-7 leftHeadingPriceSection strong">
              Total Price
            </div>
            <div className="col-md-5 rightHeadingPriceSection">
              Rs. {totalSellingPrice != -1 ? totalSellingPrice : ""}
            </div>
          </div>
          <div className="col-md-12 d-flex text-success py-2 px-0 discountText">
            {totalDiscount != -1 ? (
              <>You will save Rs. {totalDiscount} on this order</>
            ) : (
              ""
            )}
          </div>
        </>
      )}
    </div>
  );
}
