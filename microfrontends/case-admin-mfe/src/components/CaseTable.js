import { Table } from 'react-bootstrap';
import { ReactComponent as ViewIcon } from 'bootstrap-icons/icons/eye-fill.svg';
import { ReactComponent as DeleteIcon } from 'bootstrap-icons/icons/trash-fill.svg';
import { Link } from 'react-router-dom';

import deriveDisplayTextFromCaseState from '../utils/deriveDisplayTextFromCaseState';
import formatDateToIt from '../utils/formatDateToIt';


function CaseTable({ cases, onDeleteClick }) {
  return (
    <Table striped bordered hover>
      <thead>
        <tr>
          <th>Numero Domanda</th>
          <th>Data Inserimento</th>
          <th>Stato</th>
          <th>Azioni</th>
        </tr>
      </thead>
      <tbody>
        {cases.map((c) => (
          <tr key={c.id}>
            <td>{c.identifier}</td>
            <td>{formatDateToIt(c.created)}</td>
            <td>{deriveDisplayTextFromCaseState(c)}</td>
            <td className="text-end">
              <Link to={`/case/${c.id}`} style={{ color: 'inherit' }}>
                <ViewIcon />
              </Link>
              <DeleteIcon
                onClick={() => onDeleteClick(c)}
                className="ms-4 me-2"
                style={{ cursor: 'pointer' }}
              />
            </td>
          </tr>
        ))}
      </tbody>
    </Table>
  )
}

export default CaseTable;
