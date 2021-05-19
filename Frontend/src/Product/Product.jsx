import React, { useEffect, useState } from "react";
import "./Question.css";
import { useParams } from "react-router-dom";
import { useSelector, useDispatch } from "react-redux";
import {
  Row,
  Col,
  Container,
  Button,
  InputGroup,
  FormControl,
} from "react-bootstrap";
import { fetchQuestionById } from "../reducers/questionSlice";
import axios from "axios";

export default function Question() {
  let { id } = useParams();
  const [isAddAnswer, setIsAddAnswer] = useState(false);
  const dispatch = useDispatch();
  const { entityById } = useSelector((state) => state.questions);
  const { loading } = useSelector((state) => state.questions);
  useEffect(() => {
    dispatch(fetchQuestionById(id));
  }, [id]);
  return (
    <>
      {loading ? (
        "Loading..."
      ) : (
        <Container>
          <h2>{entityById.title}</h2>
          <hr />
          <Row>
            <Col md={2}>
              <h4>Tags</h4>

              {entityById &&
                entityById.tags &&
                entityById.tags.map((item) => (
                  <Col>
                    <span className="badge badge-secondary" key={item.id}>
                      {item.name}
                    </span>
                  </Col>
                ))}
            </Col>
            <Col md={10}>
              <p>
                <strong>Query:- </strong>
                {entityById.query}
              </p>
            </Col>
          </Row>
          <Row className="flexEnd">
            <Button variant="info" onClick={() => setIsAddAnswer(true)}>Add answer</Button>
          </Row>
          {isAddAnswer && (
            <Row>
              <InputGroup>
                <InputGroup.Prepend>
                  <InputGroup.Text>Answer here</InputGroup.Text>
                </InputGroup.Prepend>
                <FormControl as="textarea" aria-label="With textarea" />
              </InputGroup>
            </Row>
          )}
        </Container>
      )}
    </>
  );
}
