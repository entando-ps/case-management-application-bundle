import { useMemo } from 'react';
import { Pagination, Stack, Table } from 'react-bootstrap';
import { useTable, useSortBy, usePagination } from 'react-table';
import { ReactComponent as ViewIcon } from 'bootstrap-icons/icons/eye-fill.svg';
import { ReactComponent as DeleteIcon } from 'bootstrap-icons/icons/trash-fill.svg';
import { ReactComponent as CaretUpIcon } from 'bootstrap-icons/icons/caret-up-fill.svg';
import { ReactComponent as CaretDownIcon } from 'bootstrap-icons/icons/caret-down-fill.svg';
import { Link } from 'react-router-dom';

import deriveDisplayTextFromCaseState from '../utils/deriveDisplayTextFromCaseState';
import formatDateToIt from '../utils/formatDateToIt';

function CaseTable({ cases, onDeleteClick }) {
  const columns = useMemo(() => [{
    Header: 'Numero Domanda',
    accessor: 'identifier',
  }, {
    Header: 'Codice Fiscale',
    accessor: 'metadata.subscriber.fiscalCode',
  }, {
    Header: 'Regione',
    accessor: 'metadata.subscriber.birthRegion',
  }, {
    Header: 'Settore',
    accessor: 'metadata.subscriber.sector',
  }, {
    Header: 'Data Inserimento',
    accessor: 'created',
    Cell: ({ value }) => formatDateToIt(value),
  }, {
    Header: 'Stato',
    accessor: row => deriveDisplayTextFromCaseState(row),
  }, {
    Header: 'Azioni',
    id: 'id',
    accessor: row => row,
    disableSortBy: true,
    Cell: ({ value: c }) => (
      <div className="text-end">
        <Link to={`/case/${c.id}`} style={{ color: 'inherit' }}>
          <ViewIcon />
        </Link>
        <DeleteIcon
          onClick={() => onDeleteClick(c)}
          className="ms-4 me-2"
          style={{ cursor: 'pointer' }}
        />
      </div>
    )
  }], [onDeleteClick]);

  const initialSortBy = useMemo(() => [{
    id: 'created',
    desc: true,
  }], []);

  const {
    getTableProps,
    getTableBodyProps,
    headerGroups,
    prepareRow,
    page,
    canPreviousPage,
    canNextPage,
    pageCount,
    gotoPage,
    nextPage,
    previousPage,
    state: { pageIndex },
  } = useTable({
    columns,
    data: cases,
    disableSortRemove: true,
    initialState: {
      sortBy: initialSortBy,
    },
  }, useSortBy, usePagination);

  return (
    <>
      <Table striped bordered hover {...getTableProps()}>
        <thead>
          {headerGroups.map(headerGroup => (
            <tr {...headerGroup.getHeaderGroupProps()}>
              {headerGroup.headers.map(column => (
                <th
                  {...column.getHeaderProps(column.getSortByToggleProps())}
                >
                  <Stack direction="horizontal" gap={2} style={{ height: 24 }}>
                    {column.render('Header')}
                    {column.isSorted
                      ? column.isSortedDesc
                        ? <CaretDownIcon width={12} height={12} />
                        : <CaretUpIcon width={12} height={12} />
                      : column.canSort && (
                        <Stack style={{ color: '#ADB5BD' }}>
                          <CaretUpIcon width={12} height={12} />
                          <CaretDownIcon width={12} height={12} />
                        </Stack>
                      )
                    }
                  </Stack>
                </th>
              ))}
            </tr>
          ))}
        </thead>
        <tbody {...getTableBodyProps()}>
          {page.map(row => {
            prepareRow(row);
            return (
              <tr {...row.getRowProps()}>
                {row.cells.map(cell => {
                  return (
                    <td
                      {...cell.getCellProps()}
                    >
                      {cell.render('Cell')}
                    </td>
                  )
                })}
              </tr>
            );
          })}
        </tbody>
      </Table>
      <Pagination style={{ float: 'right' }}>
        <Pagination.First disabled={!canPreviousPage} onClick={() => gotoPage(0)} />
        <Pagination.Prev disabled={!canPreviousPage} onClick={() => previousPage()} />
        {[...Array(pageCount)].map((_, idx) => (
          <Pagination.Item
            key={idx}
            active={idx === pageIndex}
            onClick={() => gotoPage(idx)}
          >
            {idx + 1}
          </Pagination.Item>
        ))}
        <Pagination.Next disabled={!canNextPage} onClick={() => nextPage()} />
        <Pagination.Last disabled={!canNextPage} onClick={() => gotoPage(pageCount - 1)} />
      </Pagination>
    </>
  );
}

export default CaseTable;
