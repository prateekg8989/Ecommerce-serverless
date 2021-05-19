import React, { useState, useEffect } from "react";
import { Form, Row, Col, Button, InputGroup } from "react-bootstrap";
import "./AddProduct.css";
import { useSelector, useDispatch } from "react-redux";
import { productAdd, fetchS3PresignedUrl,
  uploadImage } from "../reducers/productsSlice";
export default function AddProduct() {
  const dispatch = useDispatch();
  const [productName, setProductName] = useState("");
  const [productSummary, setProductSummary] = useState("");
  const [productDesc, setProductDesc] = useState("");

  const [sellingPrice, setSellingPrice] = useState(-1);
  const [costPrice, setCostPrice] = useState(-1);
  const [quantities, setQuantities] = useState(-1);

  const [mainCategoryId, setMainCategoryId] = useState(-1);
  const [subCategoryId, setSubCategoryId] = useState(-1);
  const [imageUrls, setImageUrls] = useState("");
  const { entities } = useSelector((state) => state.categories);
  const [isImageUploaded, setIsImageUploaded] = useState(false);
  const [uniqueStringForS3, setUniqueStringForS3] = useState("");
  const [fileToUpload, setFileToUpload] = useState({});
  const { loading } = useSelector((state) => state.products);
  const { s3Url } = useSelector((state) => state.products);

  const handleFileChange = (event) => {
    const target = event.target;
    if (target.files.length !== 1) console.log();
    const reader = new FileReader();
    reader.onload = (ev) => {
      const bstr = ev.target.result;
      let blobData = new Blob([new Uint8Array(bstr)], { type: "image/jpeg" });
      setFileToUpload(blobData);
      setIsImageUploaded(true);
    };
    let file = target.files[0];
    // reader.readAsBinaryString(file);
    reader.readAsArrayBuffer(file);
    let x = new Date().valueOf();
    setUniqueStringForS3(x);
    dispatch(fetchS3PresignedUrl(x));
  };
  useEffect(() => {
    if (
      !loading &&
      s3Url &&
      s3Url.length > 5 &&
      isImageUploaded
    ) {
      let obj = {
        url: s3Url,
        file: fileToUpload,
      };
      // alert(JSON.stringify(obj));
      dispatch(uploadImage(obj));
      setTimeout(() => {
        setImageUrls(
          `https://storebazaar-images.s3.us-east-2.amazonaws.com/${uniqueStringForS3}`
        );
      },5000);
      setIsImageUploaded(false);
    }
  }, [loading, s3Url]);

  const handleSubmitClick = () => {
    if (mainCategoryId == -1 || subCategoryId == -1) {
      alert("Please select its category first!!!");
      return;
    }
    if (
      productName &&
      sellingPrice != -1 &&
      costPrice != -1 &&
      quantities != -1
    ) {
      let body = {
        mainCategoryId,
        subCategoryId,
        productName,
        productSummary,
        productDesc,
        sellingPrice,
        costPrice,
        quantities,
        imageUrls1: imageUrls.split("|"),
      };
      // alert('brfor calling api')
      // alert(JSON.stringify(body));
      dispatch(productAdd(body));
    }
  };

  return (
    <>
      <h2 className="mb-4">Add Product</h2>
      <Form>
        <Form.Group as={Row} controlId="exampleForm.ControlSelect1">
          <Form.Label column sm={2}>
            Main Category
          </Form.Label>
          <Col sm={10} className="pr-5">
            <Form.Control
              required
              as="select"
              onChange={(event) => setMainCategoryId(event.target.value)}
            >
              <option value={null}>Please select main category</option>
              {entities &&
                entities.map((item) => (
                  <option value={item.mainCategoryId} key={item.mainCategoryId}>
                    {item.mainCategoryName}
                  </option>
                ))}
            </Form.Control>
          </Col>
        </Form.Group>
        <Form.Group as={Row} controlId="exampleForm.ControlSelect2">
          <Form.Label column sm={2}>
            Sub Category
          </Form.Label>
          <Col sm={10} className="pr-5">
            <Form.Control
              required
              as="select"
              onChange={(event) => setSubCategoryId(event.target.value)}
            >
              <option value="-1">Please select sub category</option>
              {mainCategoryId != -1 &&
                entities.map(
                  (item) =>
                    item.mainCategoryId == mainCategoryId &&
                    item.subCategories.map((ele) => (
                      <option value={ele.subCategoryId} key={ele.subCategoryId}>
                        {ele.subCategoryName}
                      </option>
                    ))
                )}
            </Form.Control>
          </Col>
        </Form.Group>
        <Form.Group as={Row} controlId="exampleForm.ControlInput1">
          <Form.Label column sm={2}>
            Product Name
          </Form.Label>
          <Col sm={10} className="pr-5">
            <Form.Control
              required
              type="text"
              placeholder="Enter the name of product"
              onChange={(event) => setProductName(event.target.value)}
            />
          </Col>
        </Form.Group>

        <Form.Group as={Row} controlId="exampleForm.ControlInput2">
          <Form.Label column sm={2}>
            Product Summary
          </Form.Label>
          <Col sm={10} className="pr-5">
            <Form.Control
              required
              type="text"
              placeholder="Enter the summary of product"
              onChange={(event) => setProductSummary(event.target.value)}
            />
          </Col>
        </Form.Group>

        <Form.Group as={Row} controlId="exampleForm.ControlTextarea1">
          <Form.Label column sm={2}>
            Product Description
          </Form.Label>
          <Col sm={10} className="pr-5">
            <Form.Control
              as="textarea"
              rows={3}
              onChange={(event) => setProductDesc(event.target.value)}
            />
          </Col>
        </Form.Group>
        <Form.Group as={Row} controlId="exampleForm.ControlInput3">
          <Form.Label column sm={2}>
            Product Cost Price
          </Form.Label>
          <Col sm={10} className="pr-5">
            <Form.Control
              required
              type="number"
              placeholder="Enter the cost of product"
              onChange={(event) => setCostPrice(event.target.value)}
            />
          </Col>
        </Form.Group>

        <Form.Group as={Row} controlId="exampleForm.ControlInput4">
          <Form.Label column sm={2}>
            Product Selling Price
          </Form.Label>
          <Col sm={10} className="pr-5">
            <Form.Control
              required
              type="number"
              placeholder="Enter the selling price of product"
              onChange={(event) => setSellingPrice(event.target.value)}
            />
          </Col>
        </Form.Group>
        <Form.Group as={Row} controlId="exampleForm.ControlInput5">
          <Form.Label column sm={2}>
            Product Stock Quantity
          </Form.Label>
          <Col sm={10} className="pr-5">
            <Form.Control
              required
              type="number"
              placeholder="Enter the quantity of product"
              onChange={(event) => setQuantities(event.target.value)}
            />
          </Col>
        </Form.Group>
        <Form.Group as={Row} controlId="exampleForm.ControlInput6">
          <Form.Label column sm={2}>
            Product Image
          </Form.Label>
          <Col sm={10} className="pr-5">
          <div className="col-md-4 p-0">
                  <img
                    src={imageUrls}
                    className="img-thumbnail imageContainer1"
                  />
                </div>
                <div className="col-md-8 p-0">
                  <InputGroup>
                    <Form.File
                      label="Browse Image"
                      onChange={(event) => handleFileChange(event)}
                      custom
                      required
                      name="file"
                      label="File"
                    />
                  </InputGroup>
                </div>
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
