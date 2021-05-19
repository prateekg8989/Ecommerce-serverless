import React from "react";
import { Link } from "react-router-dom";
import { useHistory } from "react-router-dom";
import { useState, useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { signInUser } from "../reducers/userSlice";
import { Alert } from "react-bootstrap";

function Login() {
  const dispatch = useDispatch();
  const history = useHistory();
  const { loading, isError, errorMessage, isUserLoggedIn } = useSelector(
    (state) => state.users
  );

  const [username, setUserName] = useState("");
  const [password, setPassword] = useState("");
  const [errorToShow, setErrorToShow] = useState("");
  const [isSubmitClicked, setIsSubmitClicked] = useState(false);
  const handleUserName = (event) => setUserName(event.target.value);
  const handlePassword = (event) => setPassword(event.target.value);

  const handleClick = () => {
    if (username && password) {
      let body = {
        username: username,
        password: password,
      };
      setIsSubmitClicked(true);
      dispatch(signInUser(body));
    } else {
      setErrorToShow("Please enter both the fields.");
    }
  };

  useEffect(() => {
    if (!loading && isSubmitClicked && isUserLoggedIn && !isError) {
      setIsSubmitClicked(false);
      history.push("/");
    }
  }, [loading, isSubmitClicked, isUserLoggedIn, isError]);

  return (
    <form>
      <div className="col-md-6 container">
        <h3 className="text-center">Login</h3>
        <div className="form-group">
          <label>User name</label>
          <input
            type="text"
            className="form-control"
            placeholder="Enter the username"
            onChange={handleUserName}
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
        {/* <div className="form-group">
          <div className="custom-control custom-checkbox">
            <input
              type="checkbox"
              className="custom-control-input"
              id="checkbox1"
            />
            <label className="custom-control-label" htmlFor="checkbox1">
              Remember me
            </label>
          </div>
        </div> */}
        <button
          type="button"
          className="btn btn-primary btn-block"
          onClick={handleClick}
        >
          Submit
        </button>
        {isError || errorToShow != "" ? (
          <Alert className="mt-2" variant="danger">
            {errorMessage}
            {errorToShow}
          </Alert>
        ) : (
          <></>
        )}
        <p className="mt-2 forgot-password text-right">
          Forgot{" "}
          <Link className="pt-0 pr-0" to="/">
            {" "}
            password?
          </Link>
        </p>
      </div>
    </form>
  );
}

export default Login;
