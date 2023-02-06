import { Table } from 'react-bootstrap';
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
          <th colSpan={2}></th>
        </tr>
      </thead>
      <tbody>
        {cases.map((c) => (
          <tr key={c.id}>
            <td>{c.identifier}</td>
            <td>{formatDateToIt(c.created)}</td>
            <td>{deriveDisplayTextFromCaseState(c)}</td>
            <td>
              <Link to={`/case/${c.id}`}>Vedi pratica</Link>
            </td>
            <td>
              <DeleteIcon
                onClick={() => onDeleteClick(c.id)}
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
