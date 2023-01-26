import { ReactComponent as SuccessIcon } from 'bootstrap-icons/icons/check2-circle.svg';
import { Card } from 'react-bootstrap';
import { Link, useSearchParams } from 'react-router-dom';

function CaseEntrySuccess() {
  const [searchParams] = useSearchParams();
  const id = searchParams.get('id');

  return (
    <Card className="text-center">
      <Card.Body>
        <SuccessIcon className="text-success my-4" width="86" height="86" />
        <div className="mb-4">
          <h2>La tua domanda è stata inviata con successo</h2>
          <p>Di seguito il numero di pratica assegnato</p>
        </div>
        <div className="mb-3">
          <Link to="/" className="text-decoration-none">
            <big className="text-primary fw-bold">{id}</big>
          </Link>
        </div>
      </Card.Body>
    </Card>
  );
}

export default CaseEntrySuccess;
