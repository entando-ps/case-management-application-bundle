import { Stack } from 'react-bootstrap';
import { useFormContext } from 'react-hook-form';

function CaseEntryFormUploadSection() {
  const { register } = useFormContext();

  return (
    <div>
      <h4 className="mb-4">Caricamento Documenti</h4>
      <Stack direction="horizontal">
        <small>Documento</small>
        <input id="document" type="file" style={{ display: 'none' }} {...register('document')} />
        <label htmlFor="document" className="ms-auto btn btn-primary">Carica</label>
      </Stack>
    </div>
  );
}

export default CaseEntryFormUploadSection;
