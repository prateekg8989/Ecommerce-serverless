import React, { useState, useEffect } from "react";
import "./AddressList.css";
import { Button, Card, OverlayTrigger, Tooltip } from "react-bootstrap";

import { useSelector, useDispatch } from "react-redux";
import { fetchAllAddresses } from "../reducers/customerSlice";
import { useHistory } from "react-router-dom";
export default function AddressList() {
  const history = useHistory();
  const dispatch = useDispatch();
  const user = useSelector((state) => state.users);
  const { customerAddresses } = useSelector((state) => state.customer);
  const { loading } = useSelector((state) => state.customer);

  useEffect(() => {
    dispatch(fetchAllAddresses(user.userId));
  }, []);

  return (
    <div className="mt-3">
      <h2 className="mb-4">Manage Addresses</h2>
      {loading
        ? "Loading..."
        : customerAddresses &&
          customerAddresses.map((item) => (
            <div className="col-md-3">
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
          ))}
    </div>
  );
}
