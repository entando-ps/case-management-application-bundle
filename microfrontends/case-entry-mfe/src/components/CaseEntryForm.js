import { Button, Form, Stack } from 'react-bootstrap';
import { useForm, FormProvider } from 'react-hook-form';
import { useNavigate } from 'react-router-dom';

import CaseEntryFormSubjectSection from './CaseEntryFormSubjectSection';
import CaseEntryFormSubscriberSection from './CaseEntryFormSubscriberSection';
import CaseEntryFormUploadSection from './CaseEntryFormUploadSection';
import { postCase } from '../api/case';
import { useKeycloak } from '../auth/Keycloak';

function CaseEntryForm({ config }) {
  const { token } = useKeycloak();
  const formMethods = useForm();
  const navigate = useNavigate();

  const handleSubmit = async data => {
    console.log(data);
    
    try {
      const newCase = await postCase(data, config, token);
      console.log(newCase);
    } catch (error) {
      console.error(error);
    }

    navigate('/case-entry-success');
  };

  return (
    <FormProvider {...formMethods}>
      <div className="mb-5">
        <h2>Compilazione dati autorizzazione e richiesta codice dispositivo</h2>
        <p>Inserire di seguito i dati del sottoscrittore dell’autorizzazione e le informazioni del soggetto autorizzato</p>
      </div>
      <Form onSubmit={formMethods.handleSubmit(handleSubmit)}>
        <div className="mb-4">
          <CaseEntryFormSubscriberSection />
        </div>
        <div className="mb-4">
          <CaseEntryFormSubjectSection />
        </div>
        <div className="mb-4">
          <CaseEntryFormUploadSection />
        </div>
        <Stack direction="horizontal">
          <Button type="submit" className="ms-auto">Invia</Button>
        </Stack>
      </Form>
    </FormProvider>
  );
}

export default CaseEntryForm;
