import { Stack } from 'react-bootstrap';
import { ReactComponent as UploadIcon } from 'bootstrap-icons/icons/upload.svg';
import { ReactComponent as CloseIcon } from 'bootstrap-icons/icons/x-circle-fill.svg';
import { useFormContext, useWatch } from 'react-hook-form';

function CaseEntryFormUploadSection() {
  const { register, resetField } = useFormContext();
  const documents = useWatch({ name: 'documents' });

  const handleDocRemove = (idx) => {
    resetField(`documents.${idx}`);
  };

  return (
    <div>
      <h4 className="mb-4">Caricamento Documenti</h4>
      {[...Array(3)].map((_, idx) => (
        <Stack key={idx} direction="horizontal" className="mb-3">
          <small className="fw-bold">Documento {idx + 1}</small>
          <div className="ms-auto">
            {documents?.[idx]?.[0] && (
              <>
                <CloseIcon onClick={() => handleDocRemove(idx)} />
                <small className="mx-2">{documents[idx][0].name}</small>
              </>
            )}
            <input id={`document${idx}`} type="file" style={{ display: 'none' }} {...register(`documents.${idx}`)} />
            <label htmlFor={`document${idx}`} className="btn btn-light">
              <UploadIcon className="text-primary align-middle me-2" />
              <small>Carica</small>
            </label>
          </div>
        </Stack>
      ))}
    </div>
  );
}

export default CaseEntryFormUploadSection;
