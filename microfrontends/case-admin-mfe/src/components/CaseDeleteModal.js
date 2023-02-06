import { Modal, Button } from 'react-bootstrap';

function CaseDeleteModal({ container, show, caseData, onHide, onConfirm }) {
  return (
    <Modal
      container={container}
      show={show}
      onHide={onHide}
      centered
    >
      <Modal.Header closeButton>
        <Modal.Title>
          Elimina pratica
        </Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <p>
          Elimina pratica {caseData?.identifier}
        </p>
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={onHide}>
          Annulla
        </Button>
        <Button variant="danger" onClick={onConfirm}>
          Confermare
        </Button>
      </Modal.Footer>
    </Modal>
  );
}

export default CaseDeleteModal;
