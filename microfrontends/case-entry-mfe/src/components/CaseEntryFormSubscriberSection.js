import { Col, Container, Form, Row, Stack } from 'react-bootstrap';
import { useFormContext } from 'react-hook-form';

function CaseEntryFormSubscriberSection() {
  const { register } = useFormContext();

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
            <Form.Group className="mb-3" controlId="subscriberFirstName">
              <Form.Label>Nome</Form.Label>
              <Form.Control type="text" {...register('subscriberFirstName')} />
            </Form.Group>
          </Col>
          <Col md>
            <Form.Group className="mb-3" controlId="subscriberLastName">
              <Form.Label>Cognome</Form.Label>
              <Form.Control type="text" {...register('subscriberLastName')} />
            </Form.Group>
          </Col>
        </Row>
        <Row>
          <Col md>
            <Form.Group className="mb-3" controlId="subscriberBirthdate">
              <Form.Label>Data di nascita</Form.Label>
              <Form.Control type="text" {...register('subscriberBirthdate')} />
            </Form.Group>
          </Col>
          <Col md>
            <Form.Group className="mb-3" controlId="subscriberBirthCountry">
              <Form.Label>Paese di nascita</Form.Label>
              <Form.Control type="text" {...register('subscriberBirthCountry')} />
            </Form.Group>
          </Col>
        </Row>
        <Row>
          <Col md>
            <Form.Group className="mb-3" controlId="subscriberBirthplace">
              <Form.Label>Comune di nascita</Form.Label>
              <Form.Control type="text" {...register('subscriberBirthplace')} />
            </Form.Group>
          </Col>
          <Col md>
            <Form.Group className="mb-3" controlId="subscriberProvince">
              <Form.Label>Provincia</Form.Label>
              <Form.Control type="text" {...register('subscriberProvince')} />
            </Form.Group>
          </Col>
        </Row>
        <Row>
          <Col md>
            <Form.Group className="mb-3" controlId="subscriberRegion">
              <Form.Label>Regione</Form.Label>
              <Form.Control type="text" {...register('subscriberRegion')} />
            </Form.Group>
          </Col>
          <Col md>
            <Form.Group className="mb-3" controlId="subscriberFiscalCode">
              <Form.Label>Codice Fiscale</Form.Label>
              <Form.Control type="text" {...register('subscriberFiscalCode')} />
            </Form.Group>
          </Col>
        </Row>
        <Row>
          <Col md>
            <Form.Group className="mb-3" controlId="subscriberEmail">
              <Form.Label>E-mail</Form.Label>
              <Form.Control type="text" {...register('subscriberEmail')} />
            </Form.Group>
          </Col>
          <Col md>
            <Form.Group className="mb-3" controlId="subscriberFixedPhone">
              <Form.Label>Tel Fisso</Form.Label>
              <Form.Control type="text" {...register('subscriberFixedPhone')} />
            </Form.Group>
          </Col>
        </Row>
        <Row>
          <Col md>
            <Form.Group className="mb-3" controlId="subscriberMobilePhone">
              <Form.Label>Cellulare</Form.Label>
              <Form.Control type="text" {...register('subscriberMobilePhone')} />
            </Form.Group>
          </Col>
          <Col md>
            <Form.Group className="mb-3" controlId="subscriberSector">
              <Form.Label>Settore</Form.Label>
              <Form.Control type="text" {...register('subscriberSector')} />
            </Form.Group>
          </Col>
        </Row>
        <Row>
          <Col md="6">
            <Form.Group className="mb-3" controlId="subscriberDelegation">
              <Form.Label>Delega</Form.Label>
              <Form.Control type="text" {...register('subscriberDelegation')} />
            </Form.Group>
          </Col>
        </Row>
      </Container>
    </div>
  );
}

export default CaseEntryFormSubscriberSection;
