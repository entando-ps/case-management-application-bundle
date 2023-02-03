import { Card, Col, Container, Row } from 'react-bootstrap';

import { useCases } from '../hooks/useCases';
import { useDashboard } from '../hooks/useDashboard';
import CaseBarChart from './CaseBarChart';
import CasePieChart from './CasePieChart';
import CaseTable from './CaseTable';

function Dashboard({ config }) {
  const { cases } = useCases(config);
  const { dashboardData: { by_status, by_year } } = useDashboard(config);

  return (
    <div>
      <Card className="mb-5 ">
        <Card.Body>
          <h2>Benvenuto nella tua dashboard</h2>
          <p>In questa sezione potrai visualizzare lo stato delle pratiche.</p>
        </Card.Body>
      </Card>
      <Container className="mb-4" fluid>
        <Row md="2">
          <Col className="ps-0">
            <Card>
              <Card.Body>
                <CasePieChart data={by_status} />
              </Card.Body>
            </Card>
          </Col>
          <Col className="pe-0">
            <Card>
              <Card.Body>
                <CaseBarChart data={by_year} />
              </Card.Body>
            </Card>
          </Col>
        </Row>
      </Container>
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
