import React from "react";
import "./HomePage.css";
import ProductsList from "../ProductsList/ProductsList";
import { useSelector, useDispatch } from "react-redux";

function HomePage() {
  const { list } = useSelector((state) => state.products);
  const { loading } = useSelector((state) => state.products);
  const { isSearched } = useSelector((state) => state.products);
  const { searchedEntities } = useSelector((state) => state.products);
  return <ProductsList list={!isSearched && !loading && list ? [...list] : isSearched  && !loading && searchedEntities ? searchedEntities: [ ] } />;
}

export default HomePage;
