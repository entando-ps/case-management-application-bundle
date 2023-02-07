export const getDashboardData = async (config, token) => {
  const { api } = config.systemParams;

  try {
    const res = await fetch(`${api['case-api'].url}/api/cases/dashboard`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });

    if (res.ok) {
      const data = await res.json();
      return data;
    } else {
      throw new Error('Impossibile recuperare i dati del dashboard');
    }
  } catch (error) {
    throw new Error(error.message);
  }
};
