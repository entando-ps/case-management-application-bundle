import { ReactComponent as SuccessIcon } from 'bootstrap-icons/icons/check2-circle.svg';
import { Link, useSearchParams } from 'react-router-dom';

function CaseEntrySuccess() {
  const [searchParams] = useSearchParams();
  const id = searchParams.get('id');

  return (
    <div className="text-center">
      <SuccessIcon className="text-success mb-4" width="86" height="86" />
      <div className="mb-4">
        <h2>La tua domanda è stata inviata con successo</h2>
        <p>Di seguito il numero di pratica assegnato</p>
      </div>
      <Link to="/" className="text-decoration-none">
        <big className="text-primary fw-bold">{id}</big>
      </Link>
    </div>
  );
}

export default CaseEntrySuccess;
