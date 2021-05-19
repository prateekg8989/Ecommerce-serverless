import React from "react";
import { Link } from "react-router-dom";

import { useState, useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useHistory } from "react-router-dom";
import { signUpUser } from "../reducers/userSlice";
import { Alert } from "react-bootstrap";

function SignUp() {
  const dispatch = useDispatch();
  const history = useHistory();

  const [name, setName] = useState("");
  const [username, setUserName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const { isUserLoggedIn, loading } = useSelector((state) => state.users);

  if (isUserLoggedIn) {
    history.push("/");
  }
  const handleName = (event) => setName(event.target.value);
  const handleUserName = (event) => setUserName(event.target.value);
  const handleEmail = (event) => setEmail(event.target.value);
  const handlePassword = (event) => setPassword(event.target.value);
  const [errorToShow, setErrorToShow] = useState("");
  const [isSubmitClicked, setIsSubmitClicked] = useState(false);

  const handleClick = () => {
    if (name && username && email && password) {
      dispatch(
        signUpUser({
          name,
          username,
          email,
          password,
          role: "user",
        })
      );
      setIsSubmitClicked(true);
      // history.push("/sign-in");
    } else {
      setErrorToShow("Please enter all the fields.");
    }
  };

  useEffect(() => {
    if (!loading && isSubmitClicked) {
      history.push("/sign-in");
    }
  }, [loading, setIsSubmitClicked]);

  return (
    <>
      {loading ? (
        "Loading...."
      ) : (
        <form>
          <div className="col-md-6 container">
            <h3 className="text-center">Register</h3>
            <div className="form-group">
              <label>Name</label>
              <input
                type="text"
                className="form-control"
                placeholder="Enter your name"
                onChange={handleName}
              />
            </div>
            <div className="form-group">
              <label>User name</label>
              <input
                type="text"
                className="form-control"
                placeholder="Enter user name"
                onChange={handleUserName}
              />
            </div>
            <div className="form-group">
              <label>Email</label>
              <input
                type="email"
                className="form-control"
                onChange={handleEmail}
                placeholder="Enter the email address"
              />
            </div>
            <div className="form-group">
              <label>Password</label>
              <input
                type="password"
                className="form-control"
                placeholder="Enter the password"
                onChange={handlePassword}
              />
            </div>
            <button
              type="button"
              className="btn btn-primary btn-block"
              onClick={handleClick}
            >
              Register
            </button>
            {errorToShow != "" ? (
              <Alert className="mt-2" variant="danger">
                {errorToShow}
              </Alert>
            ) : (
              <></>
            )}
            <p className="mt-2 forgot-password d-flex justify-content-end">
              Already registered?
              <Link className="nav-link pt-0 pr-0" to="/">
                Login here
              </Link>
              {/* <a href="#">Login here</a> */}
            </p>
          </div>
        </form>
      )}
    </>
  );
}

export default SignUp;
