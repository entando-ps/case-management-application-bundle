import { useEffect } from 'react';
import { Button, Col, Container, Row, Stack } from 'react-bootstrap';
import { Link } from 'react-router-dom';

import { useCases } from '../hooks/useCases';

function UserArea({ config }) {
  const { isLoading, cases } = useCases(config);

  useEffect(() => {
    // fetch user/get user data from keycloak
  });

  return (
    <div>
      <div className="mb-5">
        <h2>Benvenuto nella tua Area Riservata</h2>
        <Container className="p-0" fluid>
          <Row>
            <Col xs="3">Nome: <b>Mario</b></Col>
            <Col xs="3">Data Registrazione: <b>20 Dicembre 2022</b></Col>
          </Row>
          <Row>
            <Col xs="3">Cognome: <b>Mario</b></Col>
            <Col xs="3">Numero pratica: <b>12345</b></Col>
          </Row>
        </Container>
      </div>
      <div>
        <div className="mb-4">
          <Stack direction="horizontal">
            <h3>La tua pratica</h3>
            <Link to="/case-entry" className="ms-auto">
              <Button>Crea Nuova Pratica</Button>
            </Link>
          </Stack>
        </div>
        <div>
          <h4>Compilazione dati autorizzazione e richiesta codice dispositivo</h4>
          <p className="mb-1">Data Creazione: <b>21 Dicembre 2022</b></p>
          <p>Stato: <b className="text-primary">Approvata</b></p>
          <Button>Vai alla tua pratica</Button>
        </div>
      </div>
    </div>
  );
}

export default UserArea;
