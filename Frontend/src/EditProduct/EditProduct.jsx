import React, { useState, useEffect } from "react";
import { Form, Row, Col, Button, InputGroup, Image } from "react-bootstrap";
import { useParams } from "react-router-dom";
import { faPencilAlt } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import "./EditProduct.css";
import { useSelector, useDispatch } from "react-redux";
import {
  fetchProductById,
  productEdit,
  fetchS3PresignedUrl,
  uploadImage,
} from "../reducers/productsSlice";
export default function EditProduct() {
  let { id } = useParams();
  const dispatch = useDispatch();
  const [uniqueStringForS3, setUniqueStringForS3] = useState("");
  const [fileToUpload, setFileToUpload] = useState({});
  const [productId, setProductId] = useState("");
  const [productName, setProductName] = useState("");
  const [productSummary, setProductSummary] = useState("");
  const [productDesc, setProductDesc] = useState("");

  const [sellingPrice, setSellingPrice] = useState(-1);
  const [costPrice, setCostPrice] = useState(-1);
  const [quantities, setQuantities] = useState(-1);

  const [mainCategoryId, setMainCategoryId] = useState("");
  const [subCategoryId, setSubCategoryId] = useState("");
  const [mainCategoryName, setMainCategoryName] = useState("");
  const [subCategoryName, setSubCategoryName] = useState("");
  const [imageUrls, setImageUrls] = useState("");
  const { productById } = useSelector((state) => state.products);
  const { loading } = useSelector((state) => state.products);
  const { s3Url } = useSelector((state) => state.products);

  const [isEditableProductName, setIsEditableProductName] = useState(false);
  const [isEditableProductSummary, setIsEditableProductSummary] = useState(
    false
  );
  const [isEditableProductDesc, setIsEditableProductDesc] = useState(false);

  const [isEditableSellingPrice, setIsEditableSellingPrice] = useState(false);
  const [isEditableCostPrice, setIsEditableCostPrice] = useState(false);
  const [isEditableQuantities, setIsEditableQuantities] = useState(false);

  const [isEditableImageUrls, setIsEditableImageUrls] = useState(false);
  const [isImageUploaded, setIsImageUploaded] = useState(false);

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
      isEditableImageUrls &&
      isImageUploaded
    ) {
      let obj = {
        url: s3Url,
        file: fileToUpload,
      };
      // alert(JSON.stringify(obj));
      dispatch(uploadImage(obj));
      setImageUrls(
        `https://storebazaar-images.s3.us-east-2.amazonaws.com/${uniqueStringForS3}`
      );
      setIsImageUploaded(false);
    }
  }, [loading, s3Url]);

  useEffect(() => {
    dispatch(fetchProductById(id));
  }, [id]);
  useEffect(() => {
    if (!loading && productById && (!productId || productId.length == 0)) {
      setCostPrice(productById.costPrice);
      setImageUrls(productById.imageUrls1 ? productById.imageUrls1[0] : "");
      setMainCategoryId(productById.mainCategoryId);
      setMainCategoryName(productById.mainCategoryName);
      setProductDesc(productById.productDesc);
      setProductName(productById.productName);
      setProductId(productById.productId);
      setProductSummary(productById.productSummary);
      setQuantities(productById.quantities);
      setSellingPrice(productById.sellingPrice);
      setSubCategoryId(productById.subCategoryId);
      setSubCategoryName(productById.subCategoryName);
    }
  }, [loading]);
  const handleSubmitClick = () => {
    if (
      isEditableProductName ||
      isEditableProductSummary ||
      isEditableProductDesc ||
      isEditableSellingPrice ||
      isEditableCostPrice ||
      isEditableQuantities ||
      isEditableImageUrls
    ) {
      let body = {};
      body["productId"] = productId;
      if (isEditableProductName) {
        body["productName"] = productName;
      }
      if (isEditableProductSummary) {
        body["productSummary"] = productSummary;
      }
      if (isEditableProductDesc) {
        body["productDesc"] = productDesc;
      }
      if (isEditableSellingPrice) {
        body["sellingPrice"] = sellingPrice;
      }
      if (isEditableCostPrice) {
        body["costPrice"] = costPrice;
      }
      if (isEditableQuantities) {
        body["quantities"] = quantities;
      }
      if (isEditableImageUrls) {
        body["imageUrls1"] = [imageUrls];
      }
      // alert(JSON.stringify(body));
      dispatch(productEdit(body));
    }
  };

  return (
    <>
      <h2 className="mb-4">Edit Product</h2>
      {loading ? (
        "Loading..."
      ) : (
        <Form>
          <Form.Group as={Row} controlId="exampleForm.ControlSelect1">
            <Form.Label column sm={2}>
              Main Category
            </Form.Label>
            <Col sm={10} className="pr-5">
              <Form.Control required disabled as="select">
                <option value={mainCategoryId}>{mainCategoryName}</option>
              </Form.Control>
            </Col>
          </Form.Group>
          <Form.Group as={Row} controlId="exampleForm.ControlSelect2">
            <Form.Label column sm={2}>
              Sub Category
            </Form.Label>
            <Col sm={10} className="pr-5">
              <Form.Control required disabled as="select">
                <option value={subCategoryName}>{subCategoryName}</option>
              </Form.Control>
            </Col>
          </Form.Group>
          <Form.Group as={Row} controlId="exampleForm.ControlInput1">
            <Form.Label column sm={2}>
              Product Name
            </Form.Label>
            <Col sm={10} className="pr-5">
              <InputGroup>
                <Form.Control
                  required
                  disabled={!isEditableProductName}
                  value={productName}
                  type="text"
                  placeholder="Enter the name of product"
                  onChange={(event) => setProductName(event.target.value)}
                />
                <InputGroup.Append>
                  <InputGroup.Text id="basic-addon2">
                    <FontAwesomeIcon
                      className="cursor-pointer"
                      icon={faPencilAlt}
                      onClick={() => setIsEditableProductName(true)}
                    />
                  </InputGroup.Text>
                </InputGroup.Append>
              </InputGroup>
            </Col>
          </Form.Group>

          <Form.Group as={Row} controlId="exampleForm.ControlInput2">
            <Form.Label column sm={2}>
              Product Summary
            </Form.Label>
            <Col sm={10} className="pr-5">
              <InputGroup>
                <Form.Control
                  required
                  disabled={!isEditableProductSummary}
                  value={productSummary}
                  type="text"
                  placeholder="Enter the summary of product"
                  onChange={(event) => setProductSummary(event.target.value)}
                />
                <InputGroup.Append>
                  <InputGroup.Text id="basic-addon2">
                    <FontAwesomeIcon
                      className="cursor-pointer"
                      icon={faPencilAlt}
                      onClick={() => setIsEditableProductSummary(true)}
                    />
                  </InputGroup.Text>
                </InputGroup.Append>
              </InputGroup>
            </Col>
          </Form.Group>

          <Form.Group as={Row} controlId="exampleForm.ControlTextarea1">
            <Form.Label column sm={2}>
              Product Description
            </Form.Label>
            <Col sm={10} className="pr-5">
              <InputGroup>
                <Form.Control
                  as="textarea"
                  disabled={!isEditableProductDesc}
                  value={productDesc}
                  rows={3}
                  onChange={(event) => setProductDesc(event.target.value)}
                />
                <InputGroup.Append>
                  <InputGroup.Text id="basic-addon2">
                    <FontAwesomeIcon
                      className="cursor-pointer"
                      icon={faPencilAlt}
                      onClick={() => setIsEditableProductDesc(true)}
                    />
                  </InputGroup.Text>
                </InputGroup.Append>
              </InputGroup>
            </Col>
          </Form.Group>
          <Form.Group as={Row} controlId="exampleForm.ControlInput3">
            <Form.Label column sm={2}>
              Product Cost Price
            </Form.Label>
            <Col sm={10} className="pr-5">
              <InputGroup>
                <Form.Control
                  required
                  disabled={!isEditableCostPrice}
                  value={costPrice}
                  type="number"
                  placeholder="Enter the cost of product"
                  onChange={(event) => setCostPrice(event.target.value)}
                />
                <InputGroup.Append>
                  <InputGroup.Text id="basic-addon2">
                    <FontAwesomeIcon
                      className="cursor-pointer"
                      icon={faPencilAlt}
                      onClick={() => setIsEditableCostPrice(true)}
                    />
                  </InputGroup.Text>
                </InputGroup.Append>
              </InputGroup>
            </Col>
          </Form.Group>

          <Form.Group as={Row} controlId="exampleForm.ControlInput4">
            <Form.Label column sm={2}>
              Product Selling Price
            </Form.Label>
            <Col sm={10} className="pr-5">
              <InputGroup>
                <Form.Control
                  required
                  value={sellingPrice}
                  disabled={!isEditableSellingPrice}
                  type="number"
                  placeholder="Enter the selling price of product"
                  onChange={(event) => setSellingPrice(event.target.value)}
                />
                <InputGroup.Append>
                  <InputGroup.Text id="basic-addon2">
                    <FontAwesomeIcon
                      className="cursor-pointer"
                      icon={faPencilAlt}
                      onClick={() => setIsEditableSellingPrice(true)}
                    />
                  </InputGroup.Text>
                </InputGroup.Append>
              </InputGroup>
            </Col>
          </Form.Group>
          <Form.Group as={Row} controlId="exampleForm.ControlInput5">
            <Form.Label column sm={2}>
              Product Stock Quantity
            </Form.Label>
            <Col sm={10} className="pr-5">
              <InputGroup>
                <Form.Control
                  required
                  type="number"
                  disabled={!isEditableQuantities}
                  value={quantities}
                  placeholder="Enter the quantity of product"
                  onChange={(event) => setQuantities(event.target.value)}
                />
                <InputGroup.Append>
                  <InputGroup.Text id="basic-addon2">
                    <FontAwesomeIcon
                      className="cursor-pointer"
                      icon={faPencilAlt}
                      onClick={() => setIsEditableQuantities(true)}
                    />
                  </InputGroup.Text>
                </InputGroup.Append>
              </InputGroup>
            </Col>
          </Form.Group>
          <Form.Group as={Row} controlId="exampleForm.ControlInput6">
            <Form.Label column sm={2}>
              Product Images
            </Form.Label>
            <Col sm={10} className="pr-5">
              <div className="d-flex justify-content-between align-items-center">
                <div className="col-md-4 p-0">
                  <img
                    src={imageUrls}
                    className="img-thumbnail imageContainer1"
                  />
                </div>
                <div className="col-md-8 p-0">
                  <InputGroup>
                    <Form.File
                      className={!isEditableImageUrls ? "d-none" : ""}
                      label="Browse Image"
                      onChange={(event) => handleFileChange(event)}
                      custom
                      required
                      name="file"
                      label="File"
                    />
                    {/* <Form.Control
                  required
                  value={imageUrls ? imageUrls.join("|") : ""}
                  disabled={!isEditableImageUrls}
                  type="text"
                  placeholder="Enter the image urls separated by | (pipe)"
                  onChange={(event) => setImageUrls(event.target.value)}
                /> */}
                    <InputGroup.Append>
                      <InputGroup.Text id="basic-addon2">
                        <FontAwesomeIcon
                          className="cursor-pointer"
                          icon={faPencilAlt}
                          onClick={() => setIsEditableImageUrls(true)}
                        />
                      </InputGroup.Text>
                    </InputGroup.Append>
                  </InputGroup>
                </div>
              </div>
            </Col>
          </Form.Group>
          <div className="w-100 textEnd">
            <Button variant="primary" type="button" onClick={handleSubmitClick}>
              Submit
            </Button>
          </div>
        </Form>
      )}
    </>
  );
}
