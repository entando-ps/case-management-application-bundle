import { Stack } from 'react-bootstrap';
import { ReactComponent as UploadIcon } from 'bootstrap-icons/icons/upload.svg';
import { ReactComponent as CloseIcon } from 'bootstrap-icons/icons/x-circle-fill.svg';
import { useFieldArray, useFormContext } from 'react-hook-form';

function CaseEntryFormUploadSection() {
  const { control } = useFormContext();
  const { fields, append, remove, update } = useFieldArray({
    control,
    name: "documents",
  });

  const handleFileInputChange = (e, idx) => {
    if (idx === fields.length - 1) {
      append({ 0: null});
    }
    update(idx, e.target.files);
  };

  const handleFileRemove = (idx) => {
    remove(idx);
  };

  return (
    <div>
      <h4 className="mb-4">Caricamento Documenti</h4>
      {fields.map((field, idx) => (
        <Stack key={field.id} direction="horizontal" className="mb-3">
          <small className="fw-bold">Documento {idx + 1}</small>
          <div className="ms-auto">
            {idx !== fields.length - 1 && (
              <>
                <CloseIcon onClick={() => handleFileRemove(idx)} />
                <small className="mx-2">{field[0]?.name}</small>
              </>
            )}
            <input id={field.id} type="file" style={{ display: 'none' }} onChange={(e) => handleFileInputChange(e, idx)} />
            <label htmlFor={field.id} className="btn btn-light">
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
