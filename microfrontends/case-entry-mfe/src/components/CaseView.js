import { Col, Container, ListGroup, Row, Stack } from 'react-bootstrap';
import { ReactComponent as PdfIcon } from 'bootstrap-icons/icons/file-earmark-pdf.svg';

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
  const { idTokenParsed: { given_name, family_name } = {} } = useKeycloak();
  const { caseData } = useCase(config);
  
  return (
    <div>
      <div className="mb-5">
        <h2>Compilazione dati autorizzazione e richiesta codice dispositivo</h2>
        <Container className="p-0" fluid>
          <Row>
            <Col xs="3">Nome: <b>{given_name}</b></Col>
            <Col xs="3">Data Registrazione: <b>20 Dicembre 2022</b></Col>
          </Row>
          <Row>
            <Col xs="3">Cognome: <b>{family_name}</b></Col>
            <Col xs="3">Numero pratica: <b>{caseData?.identifier}</b></Col>
          </Row>
        </Container>
      </div>
      <div className="mb-4">
        <h4 className="mb-4">Dati del sottoscrittore dell’autorizzazione<br />
          (legale rappresentante del Sogetto Proponente o suo delegato)
        </h4>
        <ListGroup variant="flush">
          {subscriberFields.map(({ label, key }) => (
            <ListGroup.Item key={key}>{label}: <b>{caseData?.metadata?.subscriber?.[key]}</b></ListGroup.Item>
          ))}
        </ListGroup>
      </div>
      <div className="mb-4">
        <h4 className="mb-4">Dati del soggetto autorizzato</h4>
        <ListGroup variant="flush">
          {authorizedFields.map(({ label, key }) => (
            <ListGroup.Item key={key}>{label}: <b>{caseData?.metadata?.authorized?.[key]}</b></ListGroup.Item>
          ))}
        </ListGroup>
      </div>
      <div className="mb-4">
        <h4 className="mb-4">Caricamento Documenti</h4>
        {caseData?.metadata?.resources?.map(({ key }, idx) => (
          <div key={key} className="mb-3">
            <Stack direction="horizontal">
              <small>Documento {idx + 1}</small>
              <div className="ms-auto">
                <small className="me-2">{key}</small>
                <PdfIcon width="22" height="30" className="text-danger" />
              </div>
            </Stack>
          </div>
        ))}
      </div>
    </div>
  )
}

export default CaseView;
