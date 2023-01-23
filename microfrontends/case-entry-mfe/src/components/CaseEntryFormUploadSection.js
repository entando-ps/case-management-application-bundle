import { Stack } from 'react-bootstrap';
import { useFormContext } from 'react-hook-form';

function CaseEntryFormUploadSection() {
  const { register } = useFormContext();

  return (
    <div>
      <h4 className="mb-4">Caricamento Documenti</h4>
      <Stack direction="horizontal">
        <small>Documento 1</small>
        <input id="document1" type="file" style={{ display: 'none' }} {...register('document1')} />
        <label htmlFor="document1" className="ms-auto btn btn-primary">Carica</label>
      </Stack>
    </div>
  );
}

export default CaseEntryFormUploadSection;
