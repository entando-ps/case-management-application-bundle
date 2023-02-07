import { Button, Card, Form, Stack, Spinner } from 'react-bootstrap';
import { useForm, FormProvider } from 'react-hook-form';
import { useNavigate } from 'react-router-dom';

import CaseEntryFormAuthorizedSection from './CaseEntryFormAuthorizedSection';
import CaseEntryFormSubscriberSection from './CaseEntryFormSubscriberSection';
import CaseEntryFormUploadSection from './CaseEntryFormUploadSection';
import { postCase } from '../api/case';
import { useKeycloak } from '../auth/Keycloak';
import { useToast } from '../contexts/ToastContext';

function CaseEntryForm({ config }) {
  const { token } = useKeycloak();
  
  const { formState, ...formMethods } = useForm({
    defaultValues: {
      documents: [{ 0: null }]
    },
  });
  const { isSubmitting } = formState;

  const navigate = useNavigate();
  const { showToast } = useToast();

  const handleSubmit = async data => {
    if (!data.documents[0][0]) {
      showToast('Deve essere allegato almeno 1 documento', 'danger')
    } else {
      try {
        const newCase = await postCase(data, config, token);
        navigate(`/case-entry-success?id=${newCase.identifier}`);
      } catch (error) {
        showToast(error.message, 'danger');
        console.error(error);
      }
    }
  };

  return (
    <FormProvider formState={formState} {...formMethods}>
      <Card className="mb-5 ">
        <Card.Body>
          <h2>Compilazione dati autorizzazione e richiesta codice dispositivo</h2>
          <p>Inserire di seguito i dati del sottoscrittore dell’autorizzazione e le informazioni del soggetto autorizzato</p>
        </Card.Body>
      </Card>
      <Form onSubmit={formMethods.handleSubmit(handleSubmit)}>
        <Card className="mb-4">
          <Card.Body>
            <CaseEntryFormSubscriberSection />
          </Card.Body>
        </Card>
        <Card className="mb-4">
          <Card.Body>
            <CaseEntryFormAuthorizedSection />
          </Card.Body>
        </Card>
        <Card className="mb-4">
          <Card.Body>
            <CaseEntryFormUploadSection />
          </Card.Body>
        </Card>
        <Card>
          <Card.Body>
            <Stack direction="horizontal">
              <Button type="submit" className="ms-auto px-5" disabled={isSubmitting}>
                {isSubmitting ? (
                  <Spinner animation="border" variant="light" size="sm" />
                ) : 'Invia'}
              </Button>
            </Stack>
          </Card.Body>
        </Card>
      </Form>
    </FormProvider>
  );
}

export default CaseEntryForm;
