import { Card, Col, Container, ListGroup, Row, Stack } from 'react-bootstrap';
import { ReactComponent as DownloadIcon } from 'bootstrap-icons/icons/download.svg';

import { useKeycloak } from '../auth/Keycloak';
import { useCase } from '../hooks/useCase';

const commonFields = [
  { label: 'Nome', key: 'name' },
  { label: 'Cognome', key: 'lastname' },
  { label: 'Data di nascita', key: 'birthDate' },
  { label: 'Paesa di nascita', key: 'birthCountry' },
  { label: 'Comune', key: 'birthCity' },
  { label: 'Provincia', key: 'birthProvince' },
  { label: 'Regione', key: 'birthRegion' },
  { label: 'Codice Fiscale', key: 'fiscalCode' },
  { label: 'E-mail', key: 'email' },
  { label: 'Cellulare', key: 'mobile' },
];

const subscriberFields = [
  ...commonFields,
  { label: 'Telefono fisso', key: 'landline' },
  { label: 'Settore', key: 'sector' },
  { label: 'Delega', key: 'delegation' },
];

const authorizedFields = [
  ...commonFields,
  { label: 'Ruolo', key: 'role' },
];

function CaseView({ config }) {
  const { idTokenParsed: { given_name, family_name, preferred_username } = {} } = useKeycloak();
  const { caseData } = useCase(config);
  
  return (
    <div>
      <Card className="mb-5">
        <Card.Body>
          <h2 className="mb-4">Compilazione dati autorizzazione e richiesta codice dispositivo</h2>
          <Container className="p-0" fluid>
            <Row className="mb-2">
              <Col xs="3">Nome: <b>{given_name || preferred_username}</b></Col>
              <Col xs>Data Registrazione: <b>20 Dicembre 2022</b></Col>
            </Row>
            <Row>
              <Col xs="3">Cognome: <b>{family_name}</b></Col>
              <Col xs>Numero pratica: <b>{caseData?.identifier}</b></Col>
            </Row>
          </Container>
        </Card.Body>
      </Card>
      <Card className="mb-4">
        <Card.Body>
          <h4 className="mb-4">Dati del sottoscrittore dell’autorizzazione<br />
            (legale rappresentante del Soggetto Proponente o suo delegato)
          </h4>
          <ListGroup variant="flush">
            {subscriberFields.map(({ label, key }) => (
              <ListGroup.Item key={key}>
                <div className="row">
                  <span className="col-lg-2 col-4">{label}: </span>
                  <b className="col-auto">{caseData?.metadata?.subscriber?.[key]}</b>
                </div>
              </ListGroup.Item>
            ))}
          </ListGroup>
        </Card.Body>
      </Card>
      <Card className="mb-4">
        <Card.Body>
          <h4 className="mb-4">Dati del soggetto autorizzato</h4>
          <ListGroup variant="flush">
            {authorizedFields.map(({ label, key }) => (
              <ListGroup.Item key={key}>
                <div className="row">
                  <span className="col-lg-2 col-4">{label}: </span>
                  <b className="col-auto">{caseData?.metadata?.authorized?.[key]}</b>
                </div>
              </ListGroup.Item>
            ))}
          </ListGroup>
        </Card.Body>
      </Card>
      <Card className="mb-4">
        <Card.Body>
          <h4 className="mb-4">Caricamento Documenti</h4>
          {caseData?.metadata?.resources?.map(({ key, url }, idx) => (
            <div key={key} className="mb-3">
              <Stack direction="horizontal">
                <small className="fw-bold">Documento {idx + 1}</small>
                <div className="ms-auto">
                  <a href={url} download style={{ color: 'inherit' }}>
                    <DownloadIcon style={{ cursor: 'pointer' }} />
                  </a>
                  <small className="mx-2">{key}</small>
                </div>
              </Stack>
            </div>
          ))}
        </Card.Body>
      </Card>
    </div>
  )
}

export default CaseView;
