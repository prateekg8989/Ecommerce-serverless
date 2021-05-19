import React, { useEffect } from "react";
import "./SubCategorySelectedPage.css";
import ProductsList from "../ProductsList/ProductsList";
import { useSelector, useDispatch } from "react-redux";
import { useParams } from "react-router-dom";
import { fetchProductsBySubCategoryId } from "../reducers/productsSlice";

function SubCategorySelectedPage() {
  let { id } = useParams();
  const { productBySubCategoryId } = useSelector((state) => state.products);
  const { loading } = useSelector((state) => state.products);
  const { isSearched } = useSelector((state) => state.products);
  const { searchedEntities } = useSelector((state) => state.products);
  const dispatch = useDispatch();
  useEffect(() => {
    dispatch(fetchProductsBySubCategoryId(id));
  }, [id]);
  return (
    <ProductsList
      list={
        !isSearched && !loading && productBySubCategoryId
          ? [...productBySubCategoryId]
          : isSearched && !loading && searchedEntities
          ? searchedEntities
          : []
      }
    />
  );
}

export default SubCategorySelectedPage;
