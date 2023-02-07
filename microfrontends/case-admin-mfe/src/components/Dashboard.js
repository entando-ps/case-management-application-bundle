import { useState, useRef } from 'react';
import { Card, Col, Container, Row } from 'react-bootstrap';
import { useNavigate } from 'react-router';
import { deleteCase } from '../api/case';
import { useKeycloak } from '../auth/Keycloak';
import { useToast } from '../contexts/ToastContext';

import { useCases } from '../hooks/useCases';
import { useDashboard } from '../hooks/useDashboard';
import CaseBarChart from './CaseBarChart';
import CaseDeleteModal from './CaseDeleteModal';
import CasePieChart from './CasePieChart';
import CaseTable from './CaseTable';

function Dashboard({ config }) {
  const { cases } = useCases(config);
  const { token } = useKeycloak();
  const { dashboardData: { by_status, by_year } } = useDashboard(config);
  const { showToast } = useToast();
  const navigate = useNavigate();
  const [deleteModalState, setDeleteModalState] = useState({
    show: false,
    caseData: null,
  });
  const deleteModalContainerRef = useRef();

  const handleCaseDeleteConfirm = async () => {
    try {
      await deleteCase(deleteModalState.caseData.id, config, token);
      showToast('Pratica eliminato', 'secondary');
      navigate(0);
    } catch (error) {
      console.error(error);
      showToast(error.message, 'danger');
    }
  };
  
  const handleCaseDeleteClick = (caseData) => {
    setDeleteModalState({
      show: true,
      caseData,
    });
  };

  const handleDeleteModalHide = () => {
    setDeleteModalState({
      show: false,
      caseData: null,
    });
  };

  return (
    <div>
      <Card className="mb-4">
        <Card.Body>
          <h2>Benvenuto nella tua dashboard</h2>
          <p>In questa sezione potrai visualizzare lo stato delle pratiche.</p>
        </Card.Body>
      </Card>
      {by_status && by_year && (
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
      )}
      <Card>
        <Card.Body>
          <h4 className="mb-4">Elenco delle pratiche</h4>
          <CaseTable cases={cases} onDeleteClick={handleCaseDeleteClick} />
        </Card.Body>
      </Card>
      <div ref={deleteModalContainerRef}>
        <CaseDeleteModal
          container={deleteModalContainerRef}
          show={deleteModalState.show}
          caseData={deleteModalState.caseData}
          onHide={handleDeleteModalHide}
          onConfirm={handleCaseDeleteConfirm}
        />
      </div>
    </div>
  )
}

export default Dashboard;
