import { Col, Container, Form, Row, Stack } from 'react-bootstrap';
import { useFormContext } from 'react-hook-form';

function CaseEntryFormAuthorizedSection() {
  const { register } = useFormContext();

  return (
    <div>
      <Stack direction="horizontal" className="mb-4">
        <h4>Dati del soggetto autorizzato</h4>
        <small className="text-muted ms-auto">*Campi Obbligatori</small>
      </Stack>
      <Container className="p-0" fluid>
        <Row>
          <Col md>
            <Form.Group className="mb-3" controlId="authorized.name">
              <Form.Label>Nome</Form.Label>
              <Form.Control type="text" {...register('authorized.name')} />
            </Form.Group>
          </Col>
          <Col md>
            <Form.Group className="mb-3" controlId="authorized.lastname">
              <Form.Label>Cognome</Form.Label>
              <Form.Control type="text" {...register('authorized.lastname')} />
            </Form.Group>
          </Col>
        </Row>
        <Row>
          <Col md>
            <Form.Group className="mb-3" controlId="authorized.birthDate">
              <Form.Label>Data di nascita</Form.Label>
              <Form.Control type="text" {...register('authorized.birthDate')} />
            </Form.Group>
          </Col>
          <Col md>
            <Form.Group className="mb-3" controlId="authorized.birthCountry">
              <Form.Label>Paese di nascita</Form.Label>
              <Form.Control type="text" {...register('authorized.birthCountry')} />
            </Form.Group>
          </Col>
        </Row>
        <Row>
          <Col md>
            <Form.Group className="mb-3" controlId="authorized.birthCity">
              <Form.Label>Comune di nascita</Form.Label>
              <Form.Control type="text" {...register('authorized.birthCity')} />
            </Form.Group>
          </Col>
          <Col md>
            <Form.Group className="mb-3" controlId="authorized.birthProvince">
              <Form.Label>Provincia</Form.Label>
              <Form.Control type="text" {...register('authorized.birthProvince')} />
            </Form.Group>
          </Col>
        </Row>
        <Row>
          <Col md>
            <Form.Group className="mb-3" controlId="authorized.birthRegion">
              <Form.Label>Regione</Form.Label>
              <Form.Control type="text" {...register('authorized.birthRegion')} />
            </Form.Group>
          </Col>
          <Col md>
            <Form.Group className="mb-3" controlId="authorized.fiscalCode">
              <Form.Label>Codice Fiscale</Form.Label>
              <Form.Control type="text" {...register('authorized.fiscalCode')} />
            </Form.Group>
          </Col>
        </Row>
        <Row>
          <Col md>
            <Form.Group className="mb-3" controlId="authorized.email">
              <Form.Label>E-mail</Form.Label>
              <Form.Control type="text" {...register('authorized.email')} />
            </Form.Group>
          </Col>
          <Col md>
            <Form.Group className="mb-3" controlId="authorized.mobile">
              <Form.Label>Cellulare</Form.Label>
              <Form.Control type="text" {...register('authorized.mobile')} />
            </Form.Group>
          </Col>
        </Row>
        <Row>
          <Col md="6">
            <Form.Group className="mb-3" controlId="authorized.role">
              <Form.Label>Ruolo</Form.Label>
              <Form.Control type="text" {...register('authorized.role')} />
            </Form.Group>
          </Col>
        </Row>
      </Container>
    </div>
  )
}

export default CaseEntryFormAuthorizedSection;
