import { useState, useEffect } from 'react';
import { useKeycloak } from '../auth/Keycloak';
import { getDashboardData } from '../api/dashboard';

export function useDashboard(config) {
  const [isLoading, setLoading] = useState(true);
  const [dashboardData, setDashboardData] = useState([]);
  const keycloak = useKeycloak();

  useEffect(() => {
    if (keycloak.authenticated) {
      if (keycloak.isTokenExpired()) {
        keycloak.login();
      } else {
        const request = async () => {
          const dashboardData = await getDashboardData(config, keycloak.token);
          
          setLoading(false);
          setDashboardData(dashboardData);
        };

        request();
      }
    }
  }, [keycloak, keycloak.authenticated, keycloak.token, config]);

  return { isLoading, dashboardData };
}
