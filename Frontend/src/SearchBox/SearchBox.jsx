import React from "react";
import "./SearchBox.css";

import { useDispatch } from "react-redux";
import { useState } from "react";
import {
  fetchSearchProducts,
  searchedCancelled,
} from "../reducers/productsSlice";

export default function SearchBox() {
  const dispatch = useDispatch();
  const [keyword, setKeyword] = useState("");

  const submitHandler = (event) => {
    //   event.preventDefault();
    if (keyword != "") {
      dispatch(
        fetchSearchProducts({
          keyword: keyword,
        })
      );
    }
  };

  const changeTextHandler = (event) => {
    if (event.target.value == "") {
      dispatch(searchedCancelled());
    }
    setKeyword(event.target.value);
  };

  return (
    <div className="row navbar-nav col-md-10">
      <div className="offset-md-2 col-md-8 ">
        <input
          className="form-control mr-sm-2 roundedCorners"
          type="search"
          onChange={changeTextHandler}
          placeholder="Search"
        />
      </div>
      <div>
        <button
          className="btn btn-success my-2 my-sm-0 roundedCorners"
          type="submit"
          onClick={submitHandler}
        >
          Search
        </button>
      </div>
    </div>
  );
}
