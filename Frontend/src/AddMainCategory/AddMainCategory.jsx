import React, { useState } from "react";
import { Form, Row, Col, Button } from "react-bootstrap";
import "./AddMainCategory.css";
import { useSelector, useDispatch } from "react-redux";
import { mainCategoryAdd } from "../reducers/categoriesSlice";
export default function AddMainCategory() {

  const dispatch = useDispatch();
  const [mainCategoryName, setMainCategoryName] = useState("");
  const [mainCategoryDesc, setMainCategoryDesc] = useState("");
  const [isDuplicate, setIsDuplicate] = useState(false);
  const { entities } = useSelector((state) => state.categories);
  const handleSubmitClick = () => {
    checkDuplicsy(mainCategoryName);
    if (mainCategoryName && !isDuplicate) {
      let body = {
        mainCategoryName,
        mainCategoryDesc,
      };
      dispatch(mainCategoryAdd(body));
    }
  };

  const handleMainCategory = (event) => {
    setMainCategoryName(event.target.value);
    checkDuplicsy(event.target.value);  
  }

  const checkDuplicsy = (text) => {
    if(entities && entities.length!=0) {
      let index = entities.findIndex((item) => item.mainCategoryName == text);
      if(index != -1 ){
        alert("This category already exist with name:- " + text + "!!!");
        setIsDuplicate(true);
      }else {
        setIsDuplicate(false);
      }
    }
  }
  return (
    <>
      <h2 className="mb-4">Add Main Category</h2>
      <Form>
        <Form.Group as={Row} controlId="exampleForm.ControlInput1">
          <Form.Label column sm={1}>
            Name
          </Form.Label>
          <Col sm={11}>
            <Form.Control
              type="text"
              placeholder="Enter the name of main category"
              onChange={handleMainCategory}
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
              onChange={(event) => setMainCategoryDesc(event.target.value)}
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
