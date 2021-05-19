import React, { useState, useEffect } from "react";
import { Form, Row, Col, Button } from "react-bootstrap";
import "./MyProfile.css";
import { useSelector, useDispatch } from "react-redux";
import {
  fetchCustomerDetails,
  updateCustomerDetails,
} from "../reducers/customerSlice";
import { useHistory } from "react-router-dom";

export default function MyProfile() {
  const history = useHistory();
  const dispatch = useDispatch();
  const { userId } = useSelector((state) => state.users);
  const { customerDetails } = useSelector((state) => state.customer);
  const { loading } = useSelector((state) => state.customer);
  const [isEditable, setIsEditable] = useState(false);
  const [name, setName] = useState("");
  const [userName, setUserName] = useState("");
  const [gender, setGender] = useState("");
  const [email, setEmail] = useState("");
  const [isSubmitClicked, setIsSubmitClicked] = useState(false);

  useEffect(() => {
    if (!loading && customerDetails) {
      setName(customerDetails.name);
      setUserName(customerDetails.username);
      setGender(customerDetails.gender);
      setEmail(customerDetails.email);
    }
    if (!loading && isEditable && isSubmitClicked) {
      setIsSubmitClicked(false);
      setIsEditable(false);
      alert("Congrats your details has been updated.");
      dispatch(fetchCustomerDetails(userId));
      history.push("/");
    }
  }, [loading, customerDetails, isSubmitClicked]);

  // useEffect(() => {
  //   dispatch(fetchCustomerDetails(userId));
  // }, []);

  const submitHandler = () => {
    let body = {
      userId,
      name,
      gender,
    };
    setIsSubmitClicked(true);
    dispatch(updateCustomerDetails(body));
  };
  return (
    <div className="mt-5">
      {!loading ? (
        <>
          <h4 className="mb-4">Hello, {name}</h4>
          <div className="offset-md-1 col-md-9">
            <div className="w-100 textEnd">
              {!isEditable && (
                <Button
                  variant="link"
                  onClick={() => setIsEditable(true)}
                  size="md"
                  className="pr-0"
                  type="button"
                >
                  Edit
                </Button>
              )}
            </div>
            <Form>
              <Form.Group as={Row} controlId="exampleForm.ControlInput1">
                <Form.Label column sm={2}>
                  Name
                </Form.Label>
                <Col sm={10}>
                  <Form.Control
                    type="text"
                    value={name}
                    disabled={!isEditable}
                    onChange={(event) => setName(event.target.value)}
                    placeholder="Enter the name"
                  />
                </Col>
              </Form.Group>
              <Form.Group as={Row} controlId="exampleForm.ControlInput2">
                <Form.Label column sm={2}>
                  Gender
                </Form.Label>
                <Col sm={10} className="d-flex align-items-center">
                  <Col sm={2} className="pl-0">
                    <Form.Check
                      type="radio"
                      value={gender}
                      checked={gender === "Male"}
                      disabled={!isEditable}
                      onClick={(event) => setGender("Male")}
                      name="gender"
                      label="Male"
                      id="Male"
                    />
                  </Col>
                  <Col sm={2} className="pl-0">
                    <Form.Check
                      type="radio"
                      value={gender}
                      checked={gender === "Female"}
                      disabled={!isEditable}
                      onClick={(event) => setGender("Female")}
                      name="gender"
                      label="Female"
                      id="Female"
                    />
                  </Col>
                </Col>
              </Form.Group>
              <Form.Group as={Row} controlId="exampleForm.ControlInput3">
                <Form.Label column sm={2}>
                  User name
                </Form.Label>
                <Col sm={10}>
                  <Form.Control
                    type="text"
                    value={userName}
                    disabled
                    placeholder="Enter the username"
                  />
                </Col>
              </Form.Group>
              <Form.Group as={Row} controlId="exampleForm.ControlInput4">
                <Form.Label column sm={2}>
                  Email Address
                </Form.Label>
                <Col sm={10}>
                  <Form.Control
                    type="text"
                    value={email}
                    disabled
                    placeholder="Enter the email address"
                  />
                </Col>
              </Form.Group>
              {isEditable && (
                <div className="w-100 textEnd">
                  <Button
                    variant="primary"
                    type="button"
                    onClick={submitHandler}
                  >
                    Submit
                  </Button>
                </div>
              )}
            </Form>
          </div>
        </>
      ) : (
        "Loading...."
      )}
    </div>
  );
}
