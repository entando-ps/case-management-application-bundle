import { useState, useEffect } from 'react';
import { useKeycloak } from '../auth/Keycloak';
import { fetchCases } from '../api/case';

export function useCases(config) {
  const [isLoading, setLoading] = useState(true);
  const [cases, setCases] = useState([]);
  const keycloak = useKeycloak();

  useEffect(() => {
    if (keycloak.authenticated) {
      if (keycloak.isTokenExpired()) {
        keycloak.login();
      } else {
        const request = async () => {
          const cases = await fetchCases(config, keycloak.token);
          
          setLoading(false);
          setCases(cases);
        };

        request();
      }
    }
  }, [keycloak, keycloak.authenticated, keycloak.token, config]);

  return { isLoading, cases };
}
