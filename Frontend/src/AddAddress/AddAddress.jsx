import React, { useState, useEffect } from "react";
import { Form, Row, Col, Button, InputGroup } from "react-bootstrap";
import "./AddAddress.css";
import { useSelector, useDispatch } from "react-redux";
import { addCustomerAddress } from "../reducers/customerSlice";
import { useHistory } from "react-router-dom";
export default function AddAddress() {
  const history = useHistory();
  const dispatch = useDispatch();
  const [recepientName, setRecepientName] = useState("");
  const [pinCode, setPinCode] = useState("");
  const [address, setAddress] = useState("");
  const [isSubmitClicked, setIsSubmitClicked] = useState(false);
  const [recepientPhoneNumber, setRecepientPhoneNumber] = useState("");
  const user = useSelector((state) => state.users);
  const { loading } = useSelector((state) => state.customer);

  const handleSubmitClick = () => {
    let body = {
      userId: user.userId,
      address: {
        recepientName,
        pinCode,
        address,
        recepientPhoneNumber,
      },
    };
    dispatch(addCustomerAddress(body));
    setIsSubmitClicked(true);
  };
  useEffect(() => {
    if (!loading && isSubmitClicked) {
      setIsSubmitClicked(false);
      history.push("/");
    }
  }, [isSubmitClicked, loading]);

  return (
    <div className="mt-3">
      <h2 className="mb-4">Add New Address</h2>
      <Form>
        <Form.Group as={Row} controlId="exampleForm.ControlInput1">
          <Form.Label column sm={2}>
            Recepient Name
          </Form.Label>
          <Col sm={10} className="pr-5">
            <Form.Control
              required
              type="text"
              placeholder="Enter the name of recepient here"
              onChange={(event) => setRecepientName(event.target.value)}
            />
          </Col>
        </Form.Group>

        <Form.Group as={Row} controlId="exampleForm.ControlInput2">
          <Form.Label column sm={2}>
            Pin Code
          </Form.Label>
          <Col sm={10} className="pr-5">
            <Form.Control
              required
              type="text"
              placeholder="Enter the pin code here"
              onChange={(event) => setPinCode(event.target.value)}
            />
          </Col>
        </Form.Group>

        <Form.Group as={Row} controlId="exampleForm.ControlTextarea1">
          <Form.Label column sm={2}>
            Complete Address
          </Form.Label>
          <Col sm={10} className="pr-5">
            <Form.Control
              as="textarea"
              placeholder="Please enter the complete address here"
              rows={3}
              onChange={(event) => setAddress(event.target.value)}
            />
          </Col>
        </Form.Group>
        <Form.Group as={Row} controlId="exampleForm.ControlInput4">
          <Form.Label column sm={2}>
            Recepient Phone
          </Form.Label>
          <Col sm={10} className="pr-5">
            <Form.Control
              required
              type="text"
              placeholder="Enter the phone number of the recepient here"
              onChange={(event) => setRecepientPhoneNumber(event.target.value)}
            />
          </Col>
        </Form.Group>
        <div className="w-100 textEnd">
          <Button variant="primary" type="button" onClick={handleSubmitClick}>
            Submit
          </Button>
        </div>
      </Form>
    </div>
  );
}
