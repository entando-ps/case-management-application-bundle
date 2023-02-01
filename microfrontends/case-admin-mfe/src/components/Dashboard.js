import { Card } from 'react-bootstrap';

import { useCases } from '../hooks/useCases';
import CaseTable from './CaseTable';

function Dashboard({ config }) {
  const { cases } = useCases(config);

  return (
    <div>
      <Card className="mb-5 ">
        <Card.Body>
          <h2>Benvenuto nella tua dashboard</h2>
          <p>In questa sezione potrai visualizzare lo stato delle pratiche.</p>
        </Card.Body>
      </Card>
      <Card>
        <Card.Body>
          <h4 className="mb-4">Elenco delle pratiche</h4>
          <CaseTable cases={cases} />
        </Card.Body>
      </Card>
    </div>
  )
}

export default Dashboard;
