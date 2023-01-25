import { Col, Container, ListGroup, Row, Stack } from 'react-bootstrap';
import { ReactComponent as PdfIcon } from 'bootstrap-icons/icons/file-earmark-pdf.svg';

function CaseView() {
  return (
    <div>
      <div className="mb-5">
        <h2>Compilazione dati autorizzazione e richiesta codice dispositivo</h2>
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
      <div className="mb-4">
        <h4 className="mb-4">Dati del sottoscrittore dell’autorizzazione<br />
          (legale rappresentante del Sogetto Proponente o suo delegato)
        </h4>
        <ListGroup variant="flush">
          <ListGroup.Item>Nome: <b></b></ListGroup.Item>
          <ListGroup.Item>Cognome: <b></b></ListGroup.Item>
          <ListGroup.Item>Data di nascita: <b></b></ListGroup.Item>
          <ListGroup.Item>Paesa di nascita: <b></b></ListGroup.Item>
          <ListGroup.Item>Comune di nascita: <b></b></ListGroup.Item>
          <ListGroup.Item>Provincia: <b></b></ListGroup.Item>
          <ListGroup.Item>Regione: <b></b></ListGroup.Item>
          <ListGroup.Item>Codice Fiscale: <b></b></ListGroup.Item>
          <ListGroup.Item>E-mail: <b></b></ListGroup.Item>
          <ListGroup.Item>Telefono fisso: <b></b></ListGroup.Item>
          <ListGroup.Item>Cellulare: <b></b></ListGroup.Item>
          <ListGroup.Item>Settore: <b></b></ListGroup.Item>
          <ListGroup.Item>Delega: <b></b></ListGroup.Item>
        </ListGroup>
      </div>
      <div className="mb-4">
        <h4 className="mb-4">Dati del soggetto autorizzato</h4>
        <ListGroup variant="flush">
          <ListGroup.Item>Nome: <b></b></ListGroup.Item>
          <ListGroup.Item>Cognome: <b></b></ListGroup.Item>
          <ListGroup.Item>Data di nascita: <b></b></ListGroup.Item>
          <ListGroup.Item>Paesa di nascita: <b></b></ListGroup.Item>
          <ListGroup.Item>Comune di nascita: <b></b></ListGroup.Item>
          <ListGroup.Item>Provincia: <b></b></ListGroup.Item>
          <ListGroup.Item>Regione: <b></b></ListGroup.Item>
          <ListGroup.Item>Codice Fiscale: <b></b></ListGroup.Item>
          <ListGroup.Item>E-mail: <b></b></ListGroup.Item>
          <ListGroup.Item>Telefono fisso: <b></b></ListGroup.Item>
          <ListGroup.Item>Cellulare: <b></b></ListGroup.Item>
          <ListGroup.Item>Ruolo: <b></b></ListGroup.Item>
        </ListGroup>
      </div>
      <div className="mb-4">
        <h4 className="mb-4">Caricamento Documenti</h4>
        <Stack direction="horizontal">
          <small>Documento</small>
          <PdfIcon width="22" height="30" className="text-danger ms-auto" />
        </Stack>
      </div>
    </div>
  )
}

export default CaseView;
