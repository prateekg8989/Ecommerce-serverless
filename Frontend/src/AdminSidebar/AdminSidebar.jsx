import React from "react";
import { useHistory } from "react-router-dom";

export default function AdminSidebar() {

  const history = useHistory();
  const handleAddMainCategoryClick = () => {
    history.push(`/add-category`);
  }

  const handleAddSubCategoryClick = () => {
    history.push(`/add-sub-category`);
  }

  const handleAddProductClick = () => {
    history.push(`/add-product`);
  }

  return (
    <ul className="nav flex-column">
      <li className="nav-item p-2 categoryTitle">
        <div className="nav-link p-0 categoryTitle">Utitilities</div>
      </li>
      <li className="nav-item p-2">
        <div className="m-0 p-0 categoryLink">
          <div className="nav-link p-0 categoryNameStyle" onClick={handleAddMainCategoryClick}>
            Add Main Category
          </div>
        </div>
      </li>
      <li className="nav-item p-2">
        <div className="m-0 p-0 categoryLink">
          <div className="nav-link p-0 categoryNameStyle" onClick={handleAddSubCategoryClick}>
            Add Sub Category
          </div>
        </div>
      </li>
      <li className="nav-item p-2">
        <div className="m-0 p-0 categoryLink">
          <div className="nav-link p-0 categoryNameStyle" onClick={handleAddProductClick}>
            Add Product
          </div>
        </div>
      </li>
    </ul>
  );
}
