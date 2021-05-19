import React, { useState } from "react";
import { Form, Row, Col, Button } from "react-bootstrap";
import "./AddSubCategory.css";
import { useSelector, useDispatch } from "react-redux";
import { subCategoryAdd } from "../reducers/categoriesSlice";
export default function AddSubCategory() {
  const dispatch = useDispatch();
  const [subCategoryName, setSubCategoryName] = useState("");
  const [subCategoryDesc, setSubCategoryDesc] = useState("");
  const [mainCategoryId, setMainCategoryId] = useState(-1);
  const [isDuplicate, setIsDuplicate] = useState(false);
  const { entities } = useSelector((state) => state.categories);
  const handleSubmitClick = () => {
    if (mainCategoryId == -1) {
      alert("Please select its base category first!!!");
      return;
    }
    checkDuplicasy(subCategoryName);
    if (subCategoryName && !isDuplicate) {
      let body = {
        subCategoryName,
        subCategoryDesc,
        mainCategoryId,
      };
      dispatch(subCategoryAdd(body));
    }
  };

  const handleSubCategory = (event) => {
    setSubCategoryName(event.target.value);
    checkDuplicasy(event.target.value);
  };

  const checkDuplicasy = (text) => {
    if (mainCategoryId != -1 && entities && entities.length != 0) {
      let mainCategory = entities.find(
        (item) => item.mainCategoryId == mainCategoryId
      );
      if (mainCategory && mainCategory.subCategories) {
        let index = mainCategory.subCategories.findIndex(
          (item) => item.subCategoryName == text
        );
        if (index != -1) {
          alert(
            "This category already exist with name:- " +
              text +
              "!!!"
          );
          setIsDuplicate(true);
        } else {
          setIsDuplicate(false);
        }
      }
    }
  }
  return (
    <>
      <h2 className="mb-4">Add Sub Category</h2>
      <Form>
        <Form.Group as={Row} controlId="exampleForm.ControlSelect1">
          <Form.Label column sm={1}>
            Main Category
          </Form.Label>
          <Col sm={11}>
            <Form.Control
              as="select"
              onChange={(event) => setMainCategoryId(event.target.value)}
            >
              <option value="-1">Please select main category</option>
              {entities &&
                entities.map((item) => (
                  <option value={item.mainCategoryId}>{item.mainCategoryName}</option>
                ))}
            </Form.Control>
          </Col>
        </Form.Group>
        <Form.Group as={Row} controlId="exampleForm.ControlInput1">
          <Form.Label column sm={1}>
            Name
          </Form.Label>
          <Col sm={11}>
            <Form.Control
              type="text"
              placeholder="Enter the name of main category"
              onChange={handleSubCategory}
            />
          </Col>
        </Form.Group>
        <Form.Group as={Row} controlId="exampleForm.ControlTextarea1">
          <Form.Label column sm={1}>
            Description
          </Form.Label>
          <Col sm={11}>
            <Form.Control
              as="textarea"
              rows={3}
              onChange={(event) => setSubCategoryDesc(event.target.value)}
            />
          </Col>
        </Form.Group>
        <div className="w-100 textEnd">
          <Button variant="primary" type="button" onClick={handleSubmitClick}>
            Submit
          </Button>
        </div>
      </Form>
    </>
  );
}
