import { Col, Container, Form, Row, Stack } from 'react-bootstrap';

function CaseEntryFormSubjectSection() {
  return (
    <div>
      <Stack direction="horizontal" className="mb-4">
        <h4>Dati del sottoscrittore dell’autorizzazione<br />
          (legale rappresentante del Sogetto Proponente o suo delegato)
        </h4>
        <small className="text-muted ms-auto">*Campi Obbligatori</small>
      </Stack>
      <Container className="p-0" fluid>
        <Row>
          <Col md>
            <Form.Group className="mb-3" controlId="subjectFirstName">
              <Form.Label>Nome</Form.Label>
              <Form.Control type="text" />
            </Form.Group>
          </Col>
          <Col md>
            <Form.Group className="mb-3" controlId="subjectLastName">
              <Form.Label>Cognome</Form.Label>
              <Form.Control type="text" />
            </Form.Group>
          </Col>
        </Row>
        <Row>
          <Col md>
            <Form.Group className="mb-3" controlId="subjectBirthdate">
              <Form.Label>Data di nascita</Form.Label>
              <Form.Control type="text" />
            </Form.Group>
          </Col>
          <Col md>
            <Form.Group className="mb-3" controlId="subjectBirthCountry">
              <Form.Label>Paese di nascita</Form.Label>
              <Form.Control type="text" />
            </Form.Group>
          </Col>
        </Row>
        <Row>
          <Col md>
            <Form.Group className="mb-3" controlId="subjectBirthplace">
              <Form.Label>Comune di nascita</Form.Label>
              <Form.Control type="text" />
            </Form.Group>
          </Col>
          <Col md>
            <Form.Group className="mb-3" controlId="subjectProvince">
              <Form.Label>Provincia</Form.Label>
              <Form.Control type="text" />
            </Form.Group>
          </Col>
        </Row>
        <Row>
          <Col md>
            <Form.Group className="mb-3" controlId="subjectRegion">
              <Form.Label>Regione</Form.Label>
              <Form.Control type="text" />
            </Form.Group>
          </Col>
          <Col md>
            <Form.Group className="mb-3" controlId="subjectFiscalCode">
              <Form.Label>Codice Fiscale</Form.Label>
              <Form.Control type="text" />
            </Form.Group>
          </Col>
        </Row>
        <Row>
          <Col md>
            <Form.Group className="mb-3" controlId="subjectEmail">
              <Form.Label>E-mail</Form.Label>
              <Form.Control type="text" />
            </Form.Group>
          </Col>
          <Col md>
            <Form.Group className="mb-3" controlId="subjectMobilePhone">
              <Form.Label>Cellulare</Form.Label>
              <Form.Control type="text" />
            </Form.Group>
          </Col>
        </Row>
        <Row>
          <Col md="6">
            <Form.Group className="mb-3" controlId="subjectRole">
              <Form.Label>Ruolo</Form.Label>
              <Form.Control type="text" />
            </Form.Group>
          </Col>
        </Row>
      </Container>
    </div>
  )
}

export default CaseEntryFormSubjectSection;
