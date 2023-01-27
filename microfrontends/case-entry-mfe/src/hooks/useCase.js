import { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';

import { useKeycloak } from '../auth/Keycloak';
import { getCase } from '../api/case';

export function useCase(config) {
  const { id } = useParams();
  const [isLoading, setLoading] = useState(true);
  const [caseData, setCaseData] = useState(null);
  const keycloak = useKeycloak();

  useEffect(() => {
    if (keycloak.authenticated) {
      if (keycloak.isTokenExpired()) {
        keycloak.login();
      } else {
        const request = async () => {
          const data = await getCase(id, config, keycloak.token);
          
          setLoading(false);
          setCaseData(data);
        };

        request();
      }
    }
  }, [id, keycloak, keycloak.authenticated, keycloak.token, config]);

  return { isLoading, caseData };
}
