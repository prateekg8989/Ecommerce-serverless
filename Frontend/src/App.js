import './App.css';
import { BrowserRouter as Router, Link, Switch, Route } from 'react-router-dom';
import Login from './Login/Login';
import SignUp from './Signup/Signup';
import HomePage from './HomePage/HomePage';
import SearchBox from './SearchBox/SearchBox';
import AddMainCategory from './AddMainCategory/AddMainCategory';
import AddSubCategory from './AddSubCategory/AddSubCategory';
import AddProduct from './AddProduct/AddProduct';
import AddAddress from './AddAddress/AddAddress';
import AddressList from './AddressList/AddressList';
import AddressSelection from './AddressSelection/AddressSelection';
import EditProduct from './EditProduct/EditProduct';
import ProductsList from './ProductsList/ProductsList';
import CategorySelectedPage from './CategorySelectedPage/CategorySelectedPage';
import SubCategorySelectedPage from './SubCategorySelectedPage/SubCategorySelectedPage';
import Sidebar from './Sidebar/Sidebar';
import MyProfile from './MyProfile/MyProfile';
import MyCart from './MyCart/MyCart';
import OrdersList from './OrdersList/OrdersList';
import AdminSidebar from './AdminSidebar/AdminSidebar';
import { useState, useEffect } from "react";
import { useSelector, useDispatch } from "react-redux";
import {
  fetchCustomerDetails
} from "./reducers/customerSlice";
import {
  getCart
} from "./reducers/cartSlice";
import { faUserCheck, faCartPlus } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Dropdown } from 'react-bootstrap';
import { setUserDetailsFromSS, logoutTheUser } from './reducers/userSlice';
import { useHistory } from "react-router-dom";

function App() {
  const history = useHistory();
  const user = useSelector(state => state.users);
  const cart = useSelector(state => state.cart);
  const dispatch = useDispatch();
  if (!user.isUserLoggedIn) {
    let tokenId = sessionStorage.getItem('tokenId');
    let username = sessionStorage.getItem('username');
    let userrole = sessionStorage.getItem("userrole");
    let userId = sessionStorage.getItem("userId");
    if (tokenId && username && userrole && userId) {
      dispatch(setUserDetailsFromSS({ tokenId, username, userrole, userId }));
    }
  }

  useEffect(() => {
    if (user && user.isUserLoggedIn && user.userId) {
      dispatch(fetchCustomerDetails(user.userId));
      dispatch(getCart(user));
    }
  }, [user]);

  const handleLogout = () => {
    dispatch(logoutTheUser());
  }
  const handleClickOnMyProfile = () => {
    history.push("/profile");
  }
  return (
    <div>
      <div className="col-md-12 p-0">
        <Router>
          <nav className="navbar navbar-expand-md navbar-dark navBgColor">
            <div className="container-fluid">
              <Link className="navbar-brand boldFont" to="/">Store Bazaar</Link>
              <div className="collapse navbar-collapse" id="navbarToggleDemo2">
                <SearchBox />
                {
                  user.isUserLoggedIn ? (
                    <div className="d-flex ml-auto">
                      <button type="button" className="btn btn-light mr-2">
                        <Link to="/cart"><FontAwesomeIcon icon={faCartPlus} /> Cart {cart && cart.cartItems && (<span className="badge badge-pill badge-primary">{cart.cartItems.length}</span>)}</Link>
                      </button>
                      <Dropdown className="ml-auto">
                        <Dropdown.Toggle variant="light" id="dropdown-basic">
                          <FontAwesomeIcon icon={faUserCheck} />
                        </Dropdown.Toggle>

                        <Dropdown.Menu>
                          <Dropdown.Item onClick={handleClickOnMyProfile} href="/profile">My Profile</Dropdown.Item>
                          <Dropdown.Item href="/addresses">Manage Addresses</Dropdown.Item>
                          <Dropdown.Item href="/orders">Orders</Dropdown.Item>
                          <Dropdown.Item onClick={handleLogout} href="/">Logout</Dropdown.Item>
                        </Dropdown.Menu>
                      </Dropdown>
                    </div>) :
                    (
                      <ul className="navbar-nav ml-auto">
                        <li className="nav-item">
                          <Link className="nav-link lightBoldFont" to="/sign-in">Log In</Link>
                        </li>
                        <li className="nav-item">
                          <Link className="nav-link lightBoldFont" to="/sign-up">Sign up</Link>
                        </li>
                      </ul>
                    )
                }


              </div>
            </div>
          </nav>
          <br />
          <div>
            <hr className="m-0" />

            <div className="col-md-12 p-0">
              <div className="d-flex">
                <div className="col-md-2 p-0">
                  {
                    user.isUserLoggedIn && user.userrole == "admin" ? (
                      <AdminSidebar />
                    )
                      : ""
                  }
                  <Sidebar />
                </div>
                <hr className="verticalLine" />
                <div className="col-md-10 ml-auto">
                  <Switch>
                    <Route exact path="/" component={HomePage} />
                    <Route exact path="/home" component={HomePage} />
                    <Route exact path="/sign-in" component={Login} />
                    <Route exact path="/sign-up" component={SignUp} />
                    <Route exact path="/add-category" component={AddMainCategory} />
                    <Route exact path="/add-sub-category" component={AddSubCategory} />
                    <Route exact path="/add-product" component={AddProduct} />
                    <Route exact path="/edit-product/:id" component={EditProduct} />
                    <Route exact path="/products" component={ProductsList} />
                    <Route exact path="/products-by-cat/:id" component={CategorySelectedPage} />
                    <Route exact path="/products-by-sub-cat/:id" component={SubCategorySelectedPage} />
                    <Route exact path="/profile" component={MyProfile} />
                    <Route exact path="/cart" component={MyCart} />
                    <Route exact path="/add-address" component={AddAddress} />
                    <Route exact path="/addresses" component={AddressList} />
                    <Route exact path="/select-address" component={AddressSelection} />
                    <Route exact path="/orders" component={OrdersList} />
                  </Switch>
                </div>
              </div>
            </div>
          </div>

        </Router>
      </div>
    </div>
  );
}

export default App;
