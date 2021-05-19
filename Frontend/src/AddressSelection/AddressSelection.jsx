import React, { useState, useEffect } from "react";
import "./AddressSelection.css";
import { Button, Card } from "react-bootstrap";
import { placeOrder } from "../reducers/orderSlice";
import CartItems from "../CartItems/CartItems";
import CartAmountSection from "../CartAmountSection/CartAmountSection";
import { useSelector, useDispatch } from "react-redux";
import { fetchAllAddresses } from "../reducers/customerSlice";
import { useHistory } from "react-router-dom";
import { faCheckCircle } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
export default function AddressSelection() {
  const history = useHistory();
  const dispatch = useDispatch();
  const user = useSelector((state) => state.users);
  const { customerAddresses } = useSelector((state) => state.customer);
  const { loading } = useSelector((state) => state.customer);
  const order = useSelector((state) => state.order);
  const [currentSelectedAddressId, setCurrentSelectedAddressId] = useState("");
  const [isSubmitButtonClicked, setIsSubmitButtonClicked] = useState(false);

  const handleAddButtonClick = () => {
    history.push("/add-address");
  };

  useEffect(() => {
    dispatch(fetchAllAddresses(user.userId));
  }, []);

  const handleAddressClicked = (custAddrId) => {
    setCurrentSelectedAddressId(custAddrId);
  };

  const handlePlaceOrderClick = () => {
    if (currentSelectedAddressId == "") {
      alert("Please select the delivery address.");
    } else {
      let body = {
        userId: user.userId,
        addressId: currentSelectedAddressId,
      };
      setIsSubmitButtonClicked(true);
      dispatch(placeOrder(body));
    }
  };

  useEffect(()=>{
    if (order && !order.loading && isSubmitButtonClicked) {
      if (order.insufficientItems && order.insufficientItems.length > 0 ) {
        alert("Order can not be placed, because of insufficient items in inventory");
      } else {
        alert("Congrats, your order has been placed");
        history.push("/");
      }
    }
  }, [order])

  return (
    <div className="d-flex mt-3">
      <div className="col-md-7">
        <div className="d-flex justify-content-between align-items-center mb-4">
          <h4 className="mb-0">Select the delivery address</h4>
          <Button
            variant="link"
            size="md"
            className="pr-0"
            type="button"
            onClick={handleAddButtonClick}
          >
            Add new address
          </Button>
        </div>
        <div className="d-flex">
          {loading
            ? "Loading..."
            : customerAddresses &&
              customerAddresses.map((item) => (
                <div key={item.custAddrId} className="col-md-6 p-0">
                  <Card
                    style={{ width: "16rem", cursor: "pointer" }}
                    onClick={() => handleAddressClicked(item.custAddrId)}
                  >
                    {currentSelectedAddressId &&
                      currentSelectedAddressId == item.custAddrId && (
                        <div className="addressSelectionTick">
                          <FontAwesomeIcon
                            className="cursor-pointer"
                            color="#007BFF"
                            icon={faCheckCircle}
                          />
                        </div>
                      )}
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
      </div>
      <div className="col-md-5">
        <div>
          <CartAmountSection />
        </div>
        <div className="text-center">
          <button
            className="btn btn-md btn-outline-primary w-75"
            onClick={handlePlaceOrderClick}
          >
            Place Order
          </button>
        </div>
        <div className="mt-3">
          <h6>Your cart details:- </h6>
          <CartItems />
        </div>
      </div>
    </div>
  );
}
