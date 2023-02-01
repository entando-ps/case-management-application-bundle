import { Button, Card, Col, Container, ListGroup, Row, Stack } from 'react-bootstrap';
import { ReactComponent as PdfIcon } from 'bootstrap-icons/icons/file-earmark-pdf.svg';
import { ReactComponent as DownloadIcon } from 'bootstrap-icons/icons/download.svg';
import { useNavigate } from 'react-router-dom';

import { useKeycloak } from '../auth/Keycloak';
import { useCase } from '../hooks/useCase';
import { approveCase, rejectCase } from '../api/case';
import { useToast } from '../contexts/ToastContext';
import deriveDisplayTextFromCaseState from '../utils/deriveDisplayTextFromCaseState';

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
  const { idTokenParsed: { given_name, family_name, preferred_username } = {}, token } = useKeycloak();
  const { caseData } = useCase(config);
  const { showToast } = useToast();
  const navigate = useNavigate();

  const handleReject = async () => {
    try {
      await rejectCase(caseData.id, config, token);
      showToast(`Pratica rifiutata ${caseData.identifier}`, 'secondary');
      navigate('/');
    } catch (error) {
      showToast(error.message, 'danger');
      console.error(error);
    }
  };

  const handleApprove = async () => {
    try {
      await approveCase(caseData.id, config, token);
      showToast(`Pratica approvata ${caseData.identifier}`, 'primary');
      navigate('/');
    } catch (error) {
      showToast(error.message, 'danger');
      console.error(error);
    }
  };
  
  return (
    <div>
      <Card className="mb-5">
        <Card.Body>
          <h2 className="mb-4">Compilazione dati autorizzazione e richiesta codice dispositivo</h2>
          <Stack direction="horizontal">
            <Container className="p-0" fluid>
              <Row className="mb-2">
                <Col xs="3">Nome: <b>{given_name || preferred_username}</b></Col>
                <Col xs>Apertura pratica: <b>20 Dicembre 2022</b></Col>
              </Row>
              <Row>
                <Col xs="3">Cognome: <b>{family_name}</b></Col>
                <Col xs>Numero pratica: <b>{caseData?.identifier}</b></Col>
              </Row>
            </Container>
            {caseData?.state === 'COMPLETED' ? (
              <b className={caseData.metadata.processData.approved ? 'text-primary': 'text-secondary'}>
                {deriveDisplayTextFromCaseState(caseData)}
              </b>
            ) : (
              <Stack direction="horizontal">
                <Button onClick={handleReject} variant="secondary" className="px-5">Rifiuta</Button>
                <Button onClick={handleApprove} className="px-5 ms-3">Approva</Button>
              </Stack>
            )}
          </Stack>
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
                  <PdfIcon width="22" height="30" className="text-danger" />
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
