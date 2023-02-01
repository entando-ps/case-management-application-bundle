import { Button, Card, Col, Container, Row, Stack } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import { useKeycloak } from '../auth/Keycloak';

import { useCases } from '../hooks/useCases';
import deriveDisplayTextFromCaseState from '../utils/deriveDisplayTextFromCaseState';
import formatDateToIt from '../utils/formatDateToIt';

function UserArea({ config }) {
  const { idTokenParsed: { given_name, family_name, preferred_username } = {} } = useKeycloak();
  const { cases } = useCases(config);

  return (
    <div>
      <Card className="mb-5">
        <Card.Body>
          <h2 className="mb-4">Benvenuto nella tua Area Riservata</h2>
          <Container className="p-0" fluid>
            <Row className="mb-2">
              <Col xs="3">Nome: <b>{given_name || preferred_username}</b></Col>
              <Col xs>Data Registrazione: <b>20 Dicembre 2022</b></Col>
            </Row>
            <Row>
              <Col xs="3">Cognome: <b>{family_name}</b></Col>
              <Col xs>Numero pratica: {cases[0]
                ? <b>{cases[0]?.identifier}</b>
                : <span className="text-muted">Nessuna pratica presentata</span>}
              </Col>
            </Row>
          </Container>
        </Card.Body>
      </Card>
      <div>
        <div className="mb-4">
          <Stack direction="horizontal" className="px-3">
            <h4>La tua pratica</h4>
            {cases.length === 0 && (
              <Link to="/case-entry" className="ms-auto">
                <Button>Crea Nuova Pratica</Button>
              </Link>
            )}
          </Stack>
          <hr className="mx-3" />
        </div>
        {cases.length > 0 && (
          <Card style={{ width: '26rem' }}>
            <Card.Body>
              <h5 className="mb-3">Compilazione dati autorizzazione e richiesta codice dispositivo</h5>
              <p className="mb-1">Data Creazione: <b>{formatDateToIt(cases[0].created)}</b></p>
              <p>Stato: <b className="text-primary">{deriveDisplayTextFromCaseState(cases[0])}</b></p>
              <Link to={`/case/${cases[0].id}`}>
                <Button>Vai alla tua pratica</Button>
              </Link>
            </Card.Body>
          </Card>
        )}
      </div>
    </div>
  );
}

export default UserArea;
