import { Table } from 'react-bootstrap';
import { Link } from 'react-router-dom';

import deriveCaseState from '../utils/deriveCaseState';
import formatDateToIt from '../utils/formatDateToIt';


function CaseTable({ cases }) {
  return (
    <Table striped bordered hover>
      <thead>
        <tr>
          <th>Numero Domanda</th>
          <th>Codice Ente</th>
          <th>Ragione Sociale</th>
          <th>Partenariato</th>
          <th>Data Inserimento</th>
          <th>Stato</th>
          <th></th>
        </tr>
      </thead>
      <tbody>
        {cases.map((c) => (
          <tr key={c.id}>
            <td>{c.identifier}</td>
            <td>08202000030</td>
            <td>Accademia Danza</td>
            <td>NO</td>
            <td>{formatDateToIt(c.created)}</td>
            <td>{deriveCaseState(c)}</td>
            <td>
              <Link to={`/case/${c.id}`}>Vedi pratica</Link>
            </td>
          </tr>
        ))}
      </tbody>
    </Table>
  )
}

export default CaseTable;
