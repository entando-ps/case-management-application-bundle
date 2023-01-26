import { Button, Col, Container, Row, Stack } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import { useKeycloak } from '../auth/Keycloak';

import { useCases } from '../hooks/useCases';
import formatDateToIt from '../utils/formatDateToIt';

const caseStates = {
  CREATED: 'Aperte',
  APPROVED: 'Approvata',
  REJECTED: 'Rifiutata',
};

function UserArea({ config }) {
  const { idTokenParsed: { given_name, family_name } = {} } = useKeycloak();
  const { cases } = useCases(config);

  return (
    <div>
      <div className="mb-5">
        <h2>Benvenuto nella tua Area Riservata</h2>
        <Container className="p-0" fluid>
          <Row>
            <Col xs="3">Nome: <b>{given_name}</b></Col>
            <Col xs="3">Data Registrazione: <b>20 Dicembre 2022</b></Col>
          </Row>
          <Row>
            <Col xs="3">Cognome: <b>{family_name}</b></Col>
            <Col xs="3">Numero pratica: <b>{cases[0]?.identifier}</b></Col>
          </Row>
        </Container>
      </div>
      <div>
        <div className="mb-4">
          <Stack direction="horizontal">
            <h3>La tua pratica</h3>
            {cases.length === 0 && (
              <Link to="/case-entry" className="ms-auto">
                <Button>Crea Nuova Pratica</Button>
              </Link>
            )}
          </Stack>
        </div>
        {cases.length > 0 && (
          <div>
            <h4>Compilazione dati autorizzazione e richiesta codice dispositivo</h4>
            <p className="mb-1">Data Creazione: <b>{formatDateToIt(cases[0].created)}</b></p>
            <p>Stato: <b className="text-primary">{caseStates[cases[0].state]}</b></p>
            <Link to={`/case/${cases[0].id}`}>
              <Button>Vai alla tua pratica</Button>
            </Link>
          </div>
        )}
      </div>
    </div>
  );
}

export default UserArea;
