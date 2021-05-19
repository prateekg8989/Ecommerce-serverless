import React from "react";
import "./ProductsList.css";
import { useHistory } from "react-router-dom";
import { Button, Card, OverlayTrigger, Tooltip } from "react-bootstrap";
import { useEffect, useState } from "react";
import { deleteProductById } from "../reducers/productsSlice";
import { setUserDetailsFromSS } from "../reducers/userSlice";
import {
  getCart,
  addItemToCart,
  updateProductQuantity,
  removeItemFromCart,
} from "../reducers/cartSlice";
import {
  faTrash,
  faPlusCircle,
  faMinusCircle,
} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { useSelector, useDispatch } from "react-redux";
function ProductsList(props) {
  const history = useHistory();
  let [listTwoDimension, setListTwoDimension] = useState([]);
  const user = useSelector((state) => state.users);
  const dispatch = useDispatch();
  if (user && !user.isUserLoggedIn) {
    let tokenId = sessionStorage.getItem("tokenId");
    let username = sessionStorage.getItem("username");
    let userrole = sessionStorage.getItem("userrole");
    if (tokenId && username && userrole) {
      dispatch(setUserDetailsFromSS({ tokenId, username, userrole }));
    }
  }
  useEffect(() => {
    if (user && user.isUserLoggedIn) {
      dispatch(getCart(user));
    }
  }, [user]);
  const { isSearched } = useSelector((state) => state.products);
  const { loading } = useSelector((state) => state.products);
  const userCart = useSelector((state) => state.cart);

  const handleAddCartButtonClickedForUser = (product) => {
    let item = {
      productId: product.productId,
      productName: product.productName,
      sellingPrice: product.sellingPrice,
      costPrice: product.costPrice,
      imageUrls1: product.imageUrls1,
      quantities: 1,
    };
    item.quantities = 1;
    let body = {
      userId: user.userId,
      item: item,
    };
    dispatch(addItemToCart(body));
  };

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
  useEffect(() => {
    console.log("PRATEEEK calculation");
    if (props.list) {
      let bigArray = [];
      for (let index = 0; index < props.list.length; index = index + 4) {
        let arr = [];
        for (let i = index; i < index + 4; i++) {
          if (props.list[i]) {
            arr.push(props.list[i]);
          }
        }
        bigArray.push(arr);
      }
      setListTwoDimension(bigArray);
      console.log("Calulatedarr", listTwoDimension);
    }
  }, [loading, isSearched]);
  const handleEditClick = (id) => {
    history.push(`/edit-product/${id}`);
  };
  const handleDeleteClick = (productId) => {
    // alert("product to be deleted " + productId);
    dispatch(deleteProductById(productId));
  };
  return (
    <>
      {loading || userCart.loading ? (
        "Loading..."
      ) : (
        <div className="col-md-12 p-0 mt-3">
          {listTwoDimension &&
            listTwoDimension.length > 0 &&
            listTwoDimension.map((arr3Elements, i1) => (
              <div key={i1} className="col-md-12 d-flex p-0 m-0 mb-3">
                {arr3Elements &&
                  arr3Elements.map((item) => (
                    <div key={item.productId} className="col-md-3">
                      <Card
                        className={
                          user.isUserLoggedIn
                            ? user.userrole == "admin"
                              ? "productContainerAdmin"
                              : "productContainerCustomer"
                            : "productContainerCustomer"
                        }
                      >
                        <div className="d-flex justify-content-between">
                          <span className="badge badge-primary discountBadge m-2">
                            {parseInt(
                              ((item.costPrice - item.sellingPrice) * 100) /
                                item.costPrice
                            )}
                            % OFF
                          </span>
                          {user.isUserLoggedIn ? (
                            user.userrole == "admin" ? (
                              <span className="badge rounded-pill badge-secondary discountBadge m-2">
                                {item.quantities} Left
                              </span>
                            ) : (
                              ""
                            )
                          ) : (
                            ""
                          )}
                        </div>
                        <div className="imageContainer">
                          <Card.Img
                            className="imageCss"
                            variant="top"
                            src={item.imageUrls1 ? item.imageUrls1[0] : ""}
                          />
                        </div>
                        <Card.Body>
                          <Card.Title className="font16 shortText">
                            <OverlayTrigger
                              placement="bottom-start"
                              trigger="focus"
                              // delay={{ show: 250, hide: 400 }}
                              overlay={
                                <Tooltip id={`tooltip-${item.productId}`}>
                                  Prateek
                                </Tooltip>
                              }
                            >
                              <p className="shortText m-0">
                                {item.productName}
                              </p>
                            </OverlayTrigger>
                          </Card.Title>
                          <Card.Subtitle className="mb-2 text-muted font14 shortText">
                            {item.productSummary}
                          </Card.Subtitle>
                          <div className="d-flex align-items-center justify-content-between">
                            <div>
                              <div className="font14 text-muted striked">
                                {" "}
                                Rs. {item.costPrice}
                              </div>
                              <div className="font16 text-primary d-flex align-items-center">
                                Rs. {item.sellingPrice} &nbsp;
                                <div className="font14 text-muted">
                                  {parseInt(
                                    ((item.costPrice - item.sellingPrice) *
                                      100) /
                                      item.costPrice
                                  )}
                                  % OFF
                                </div>
                              </div>
                            </div>
                            <div>
                              {!user.isUserLoggedIn ? (
                                <button
                                  className="btn btn-primary btn-sm"
                                  onClick={() => alert("Please login first!!!")}
                                >
                                  Add
                                </button>
                              ) : userCart &&
                                userCart.cartItems &&
                                userCart.cartItems.length > 0 ? (
                                userCart.cartItems.findIndex(
                                  (ele) => ele.productId == item.productId
                                ) != -1 ? (
                                  <div className="d-flex p-0 justify-content-between">
                                    <div>
                                      <FontAwesomeIcon
                                        className="cursor-pointer"
                                        icon={faMinusCircle}
                                        onClick={() =>
                                          handleMinusCartClicked(item.productId)
                                        }
                                      />
                                    </div>
                                    <div className="px-1">
                                      {
                                        userCart.cartItems.find(
                                          (ele) =>
                                            ele.productId == item.productId
                                        ).quantities
                                      }
                                    </div>
                                    <div>
                                      <FontAwesomeIcon
                                        color="blue"
                                        className="cursor-pointer"
                                        icon={faPlusCircle}
                                        onClick={() =>
                                          handlePlusCartClicked(item.productId)
                                        }
                                      />
                                    </div>
                                  </div>
                                ) : (
                                  <button
                                    className="btn btn-primary btn-sm"
                                    onClick={() =>
                                      handleAddCartButtonClickedForUser(item)
                                    }
                                  >
                                    Add
                                  </button>
                                )
                              ) : (
                                <button
                                  className="btn btn-primary btn-sm"
                                  onClick={() =>
                                    handleAddCartButtonClickedForUser(item)
                                  }
                                >
                                  Add
                                </button>
                              )}
                            </div>
                          </div>
                        </Card.Body>
                        {user.isUserLoggedIn && user.userrole == "admin" ? (
                          <Card.Footer className="text-muted">
                            <div className="d-flex align-items-center justify-content-between">
                              <button
                                onClick={() => handleEditClick(item.productId)}
                                className="btn btn-secondary btn-sm w-75"
                              >
                                Edit
                              </button>
                              <div
                                className="cursor-pointer"
                                onClick={() =>
                                  handleDeleteClick(item.productId)
                                }
                              >
                                <FontAwesomeIcon
                                  className="cursor-pointer"
                                  icon={faTrash}
                                />
                              </div>
                            </div>
                          </Card.Footer>
                        ) : (
                          ""
                        )}
                      </Card>
                    </div>
                  ))}
              </div>
            ))}
        </div>
      )}
    </>
  );
}

export default ProductsList;
