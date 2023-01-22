import { useRef } from 'react';
import { Button, Stack } from 'react-bootstrap';

function CaseEntryFormUploadSection() {
  const fileInputRef = useRef(null);

  return (
    <div>
      <h4 className="mb-4">Caricamento Documenti</h4>
      <Stack direction="horizontal">
        <small>Documento 1</small>
        <input ref={fileInputRef} type="file" onChange={() => {}} style={{ display: 'none' }} />
        <Button onClick={() => fileInputRef.current.click()} className="ms-auto">Carica</Button>
      </Stack>
    </div>
  );
}

export default CaseEntryFormUploadSection;
