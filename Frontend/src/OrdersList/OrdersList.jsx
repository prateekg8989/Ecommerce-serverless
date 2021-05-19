import React, { useState, useEffect } from "react";
import "./OrdersList.css";
import { Button, Card, Accordion } from "react-bootstrap";

import { useSelector, useDispatch } from "react-redux";
import { fetchOrders } from "../reducers/orderSlice";
import { useHistory } from "react-router-dom";
export default function OrdersList() {
  const history = useHistory();
  const dispatch = useDispatch();
  const user = useSelector((state) => state.users);
  const { orders, loading } = useSelector((state) => state.order);
  useEffect(() => {
    if (user && !user.loading) {
      dispatch(fetchOrders(user.userId));
    }
  }, [user]);

  return (
    <div className="mt-3">
      <h2 className="mb-4">Your orders</h2>
      {loading
        ? "Loading..."
        : orders &&
          orders.map((item, index) => (
            <Accordion key={item.orderId}>
              <Card>
                <Card.Header>
                  <Accordion.Toggle
                    as={Card.Header}
                    variant="link"
                    eventKey={item.orderId}
                  >
                    <p>{" "}
                    {index + 1}) {" "}
                    Order Date:- {new Date(
                      item.orderDate
                    ).toLocaleString()}{"    "}&nbsp;
                    Order Amount:- Rs.{item.totalAmount}</p>
                  </Accordion.Toggle>
                </Card.Header>
                <Accordion.Collapse eventKey={item.orderId}>
                  <Card.Body>
                    <div className="d-flex">
                      <div className="col-md-7">
                        {item.orderItem &&
                          item.orderItem.map((ele) => (
                            <div
                              key={ele.productId}
                              className="d-flex itemContainer py-3 mb-2"
                            >
                              <div className="col-md-4">
                                <img
                                  className="image"
                                  src={ele.imageUrls1 ? ele.imageUrls1[0] : ""}
                                />
                              </div>
                              <div className="col-md-8 d-flex">
                                <div className="col-md-7">
                                  <p className="font-weight-bold">
                                    {ele.productName}
                                  </p>
                                  <p className="text-muted">
                                    {ele.productSummary}
                                  </p>
                                  <div>
                                    <div className="font14 text-muted striked">
                                      {" "}
                                      Rs. {ele.costPrice}
                                    </div>
                                    <div className="font16 text-primary d-flex align-items-center">
                                      Rs. {ele.sellingPrice} &nbsp;
                                      <div className="font14 text-muted">
                                        {parseInt(
                                          ((ele.costPrice - ele.sellingPrice) *
                                            100) /
                                            ele.costPrice
                                        )}
                                        % OFF
                                      </div>
                                    </div>
                                  </div>

                                  <div className="d-flex p-0 justify-content-between w-50">
                                    Quantities:- {ele.quantities}
                                  </div>
                                </div>
                              </div>
                            </div>
                          ))}
                      </div>
                      <div className="col-md-5">
                        <h5>Delivery address:-</h5>
                        <Card style={{ width: "18rem" }}>
                          <Card.Body>
                            <Card.Title>{item.recepientName}</Card.Title>
                            <Card.Subtitle className="mb-2 text-muted">
                              Phone {item.recepientPhoneNumber}
                            </Card.Subtitle>
                            <Card.Text>{item.address}</Card.Text>
                          </Card.Body>
                          <Card.Footer className="text-muted">
                            Pin:- {item.pinCode}
                          </Card.Footer>
                        </Card>
                      </div>
                    </div>
                  </Card.Body>
                </Accordion.Collapse>
              </Card>
            </Accordion>
          ))}
    </div>
  );
}
