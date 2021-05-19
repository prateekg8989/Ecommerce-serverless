import React, { useEffect } from "react";
import "./CategorySelectedPage.css";
import ProductsList from "../ProductsList/ProductsList";
import { useSelector, useDispatch } from "react-redux";
import { useParams } from "react-router-dom";
import { fetchProductsByCategoryId } from "../reducers/productsSlice";

function CategorySelectedPage() {
  let { id } = useParams();
  const { productByCategoryId } = useSelector((state) => state.products);
  const { loading } = useSelector((state) => state.products);
  const { isSearched } = useSelector((state) => state.products);
  const { searchedEntities } = useSelector((state) => state.products);
  const dispatch = useDispatch();
  useEffect(() => {
    dispatch(fetchProductsByCategoryId(id));
  }, [id]);
  return (
    <ProductsList
      list={
        !isSearched && !loading && productByCategoryId
          ? [...productByCategoryId]
          : isSearched && !loading && searchedEntities
          ? searchedEntities
          : []
      }
    />
  );
}

export default CategorySelectedPage;
