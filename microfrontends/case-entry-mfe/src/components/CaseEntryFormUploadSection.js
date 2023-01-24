import { Stack } from 'react-bootstrap';
import { ReactComponent as UploadIcon } from 'bootstrap-icons/icons/upload.svg';
import { useFormContext } from 'react-hook-form';

function CaseEntryFormUploadSection() {
  const { register } = useFormContext();

  return (
    <div>
      <h4 className="mb-4">Caricamento Documenti</h4>
      <Stack direction="horizontal">
        <small>Documento</small>
        <input id="document" type="file" style={{ display: 'none' }} {...register('document')} />
        <label htmlFor="document" className="ms-auto btn btn-light">
          <UploadIcon className="text-primary align-middle me-2" />
          <small>Carica</small>
        </label>
      </Stack>
    </div>
  );
}

export default CaseEntryFormUploadSection;
