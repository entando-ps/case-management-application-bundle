import { Form } from 'react-bootstrap';

import CaseEntryFormSubjectSection from './CaseEntryFormSubjectSection';
import CaseEntryFormSubscriberSection from './CaseEntryFormSubscriberSection';
import CaseEntryFormUploadSection from './CaseEntryFormUploadSection';

function CaseEntryForm() {
  return (
    <div>
      <div className="mb-5">
        <h2>Compilazione dati autorizzazione e richiesta codice dispositivo</h2>
        <p>Inserire di seguito i dati del sottoscrittore dell’autorizzazione e le informazioni del soggetto autorizzato</p>
      </div>
      <Form>
        <div className="mb-4">
          <CaseEntryFormSubscriberSection />
        </div>
        <div className="mb-4">
          <CaseEntryFormSubjectSection />
        </div>
        <div className="mb-4">
          <CaseEntryFormUploadSection />
        </div>
      </Form>
    </div>
  );
}

export default CaseEntryForm;
