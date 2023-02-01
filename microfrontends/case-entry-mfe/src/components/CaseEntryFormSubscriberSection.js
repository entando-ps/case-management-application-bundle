import { Col, Container, Form, Row, Stack } from 'react-bootstrap';
import { useFormContext } from 'react-hook-form';

function CaseEntryFormSubscriberSection() {
  const { register } = useFormContext();

  return (
    <div>
      <Stack direction="horizontal" className="mb-4">
        <h4>Dati del sottoscrittore dell’autorizzazione<br />
          (legale rappresentante del Soggetto Proponente o suo delegato)
        </h4>
        <small className="text-muted ms-auto">*Campi Obbligatori</small>
      </Stack>
      <Container className="p-0" fluid>
        <Row>
          <Col md>
            <Form.Group className="mb-3" controlId="subscriber.name">
              <Form.Label>Nome</Form.Label>
              <Form.Control type="text" {...register('subscriber.name')} />
            </Form.Group>
          </Col>
          <Col md>
            <Form.Group className="mb-3" controlId="subscriber.lastname">
              <Form.Label>Cognome</Form.Label>
              <Form.Control type="text" {...register('subscriber.lastname')} />
            </Form.Group>
          </Col>
        </Row>
        <Row>
          <Col md>
            <Form.Group className="mb-3" controlId="subscriber.birthDate">
              <Form.Label>Data di nascita</Form.Label>
              <Form.Control type="text" {...register('subscriber.birthDate')} />
            </Form.Group>
          </Col>
          <Col md>
            <Form.Group className="mb-3" controlId="subscriber.birthCountry">
              <Form.Label>Paese di nascita</Form.Label>
              <Form.Control type="text" {...register('subscriber.birthCountry')} />
            </Form.Group>
          </Col>
        </Row>
        <Row>
          <Col md>
            <Form.Group className="mb-3" controlId="subscriber.birthCity">
              <Form.Label>Comune di nascita</Form.Label>
              <Form.Control type="text" {...register('subscriber.birthCity')} />
            </Form.Group>
          </Col>
          <Col md>
            <Form.Group className="mb-3" controlId="subscriber.birthProvince">
              <Form.Label>Provincia</Form.Label>
              <Form.Control type="text" {...register('subscriber.birthProvince')} />
            </Form.Group>
          </Col>
        </Row>
        <Row>
          <Col md>
            <Form.Group className="mb-3" controlId="subscriber.birthRegion">
              <Form.Label>Regione</Form.Label>
              <Form.Control type="text" {...register('subscriber.birthRegion')} />
            </Form.Group>
          </Col>
          <Col md>
            <Form.Group className="mb-3" controlId="subscriber.fiscalCode">
              <Form.Label>Codice Fiscale</Form.Label>
              <Form.Control type="text" {...register('subscriber.fiscalCode')} />
            </Form.Group>
          </Col>
        </Row>
        <Row>
          <Col md>
            <Form.Group className="mb-3" controlId="subscriber.email">
              <Form.Label>E-mail</Form.Label>
              <Form.Control type="text" {...register('subscriber.email')} />
            </Form.Group>
          </Col>
          <Col md>
            <Form.Group className="mb-3" controlId="subscriber.landline">
              <Form.Label>Tel Fisso</Form.Label>
              <Form.Control type="text" {...register('subscriber.landline')} />
            </Form.Group>
          </Col>
        </Row>
        <Row>
          <Col md>
            <Form.Group className="mb-3" controlId="subscriber.mobile">
              <Form.Label>Cellulare</Form.Label>
              <Form.Control type="text" {...register('subscriber.mobile')} />
            </Form.Group>
          </Col>
          <Col md>
            <Form.Group className="mb-3" controlId="subscriber.sector">
              <Form.Label>Settore</Form.Label>
              <Form.Control type="text" {...register('subscriber.sector')} />
            </Form.Group>
          </Col>
        </Row>
        <Row>
          <Col md="6">
            <Form.Group className="mb-3" controlId="subscriber.delegation">
              <Form.Label>Delega</Form.Label>
              <Form.Select {...register('subscriber.delegation')}>
                <option value="" hidden></option>
                <option value="TIPO_UNO">TIPO_UNO</option>
                <option value="TIPO_DUE">TIPO_DUE</option>
              </Form.Select>
            </Form.Group>
          </Col>
        </Row>
      </Container>
    </div>
  );
}

export default CaseEntryFormSubscriberSection;
