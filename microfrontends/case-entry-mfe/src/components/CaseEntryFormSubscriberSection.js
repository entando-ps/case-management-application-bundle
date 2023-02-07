import { Col, Container, Form, Row, Stack } from 'react-bootstrap';
import { useFormContext } from 'react-hook-form';

function CaseEntryFormSubscriberSection() {
  const { register, formState: { errors } } = useFormContext();

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
              <Form.Control type="date" {...register('subscriber.birthDate')} />
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
              <Form.Control
                type="text"
                isInvalid={!!errors.subscriber?.fiscalCode}
                {...register('subscriber.fiscalCode', { pattern: /^[A-Z]{6}\d{2}[A-Z]\d{2}[A-Z]\d{3}[A-Z]$/ })}
              />
            </Form.Group>
          </Col>
        </Row>
        <Row>
          <Col md>
            <Form.Group className="mb-3" controlId="subscriber.email">
              <Form.Label>E-mail</Form.Label>
              <Form.Control
                type="text"
                isInvalid={!!errors.subscriber?.email}
                {...register('subscriber.email', { pattern: /^[^\s@]+@[^\s@]+\.[^\s@]+$/ })}
              />
            </Form.Group>
          </Col>
          <Col md>
            <Form.Group className="mb-3" controlId="subscriber.landline">
              <Form.Label>Tel Fisso</Form.Label>
              <Form.Control
                type="text"
                isInvalid={!!errors.subscriber?.landline}
                {...register('subscriber.landline', { pattern: /^[+]?[(]?[0-9]{3}[)]?[-\s.]?[0-9]{3}[-\s.]?[0-9]{4,6}$/ })}
              />
            </Form.Group>
          </Col>
        </Row>
        <Row>
          <Col md>
            <Form.Group className="mb-3" controlId="subscriber.mobile">
              <Form.Label>Cellulare</Form.Label>
              <Form.Control
                type="text"
                isInvalid={!!errors.subscriber?.mobile}
                {...register('subscriber.mobile', { pattern: /^[+]?[(]?[0-9]{3}[)]?[-\s.]?[0-9]{3}[-\s.]?[0-9]{4,6}$/ })}
              />
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
                <option value="LEGALE_RAPPRESENTANTE">Legale rappresentante</option>
                <option value="PROCURATORE">Procuratore</option>
              </Form.Select>
            </Form.Group>
          </Col>
        </Row>
      </Container>
    </div>
  );
}

export default CaseEntryFormSubscriberSection;
