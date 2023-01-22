import { Button, Col, Container, Row, Stack } from 'react-bootstrap';

function WelcomeArea() {
  return (
    <div>
      <div className="mb-5">
        <h1>Benvenuto nella tua Area Riservata</h1>
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
        <div>
          <Stack direction="horizontal">
            <h3>La tua pratica</h3>
            <Button className="ms-auto">Crea Nuova Pratica</Button>
          </Stack>
        </div>
      </div>
    </div>
  );
}

export default WelcomeArea;
