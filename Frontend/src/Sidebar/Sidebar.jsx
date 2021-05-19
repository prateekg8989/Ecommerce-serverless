import React from "react";
import { Link } from "react-bootstrap";
import './Sidebar.css';
import { useState } from "react";
import {  useSelector } from "react-redux";
import { useHistory } from "react-router-dom";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faPlus, faMinus } from "@fortawesome/free-solid-svg-icons";

export default function Sidebar() {
  const history = useHistory();
  const [currentSelectedMainCategoryId, setCurrentSelectedMainCategoryId] = useState(-1);
  const [currentSelectedSubCategoryId, setCurrentSelectedSubCategoryId] = useState(-1);
  const [plusClicked, setPlusClicked] = useState(false);
  const { entities } = useSelector(state => state.categories);
  const handleMainCategoryClick = (mainCategory) => {
    setCurrentSelectedMainCategoryId(mainCategory.mainCategoryId);
    // alert(mainCategory.mainCategoryId);
    setPlusClicked(false);
    history.push(`/products-by-cat/${mainCategory.mainCategoryId}`);
  }
  const handleSubCategoryClick = (subCategory) => {
    setCurrentSelectedSubCategoryId(subCategory.subCategoryId);
    // history.push("/products-by-sub-cat/" + subCategory.subCategoryId);
    // alert("subcat selected:- " + subCategory.subCategoryId)
    history.push(`/products-by-sub-cat/${subCategory.subCategoryId}`);
  }

  const handlePlusClick = (mainCategory) => {
    if (!plusClicked) {
      setCurrentSelectedMainCategoryId(mainCategory.mainCategoryId);
      setPlusClicked(true);
    } else {
      setPlusClicked(false);
    }
    // alert(mainCategory.mainCategoryId)
  }
  return (
    <ul className="nav flex-column">
      <li className="nav-item p-2 categoryTitle">
        <div className="nav-link p-0 categoryTitle">Categories</div>
      </li>
      {entities &&
        entities.map((item) => (
          <li className="nav-item p-2" key={item.mainCategoryId}>
            <div className="m-0 p-0 categoryLink">
              <div
                className="nav-link p-0 categoryNameStyle"
                onClick={() => handleMainCategoryClick(item)}
              >
                {item.mainCategoryName}
              </div>
              <FontAwesomeIcon
                className="plusSign"
                onClick={() => handlePlusClick(item)}
                icon={
                  plusClicked &&
                  currentSelectedMainCategoryId == item.mainCategoryId
                    ? faMinus
                    : faPlus
                }
              />
            </div>
            {plusClicked &&
              currentSelectedMainCategoryId == item.mainCategoryId &&
              item.subCategories && (
                <div className="pl-2 mt-1">
                  {item.subCategories.map((subCat) => (
                    <div
                      key={subCat.subCategoryId}
                      onClick={() => handleSubCategoryClick(subCat)}
                    >
                      <input
                        type="radio"
                        name={item.mainCategoryName}
                        id={subCat.subCategoryId}
                        name="subCategory"
                        value={subCat}
                      />
                      <label className="m-0 ml-3" for={subCat.subCategoryId}>
                        {subCat.subCategoryName}
                        {/* <Link to={`/products-by-sub-cat/${subCat.subCategoryId}`}>{subCat.subCategoryName}</Link> */}
                      </label>
                    </div>
                  ))}
                </div>
              )}
          </li>
        ))}
    </ul>
  );
}
