export const getCases = async (config, token) => {
  const { api } = config.systemParams;

  try {
    const res = await fetch(`${api['case-api'].url}/api/cases`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });

    if (res.ok) {
      const data = await res.json();
      return data;
    } else {
      throw new Error('Impossibile recuperare i casi');
    }
  } catch (error) {
    throw new Error(error.message);
  }
};

export const postCase = async (data, config, token) => {
  const { api } = config.systemParams;

  const formData = new FormData();
  formData.append('metadata', '');
  formData.append('attachments', '');
  
  try {
    const res = await fetch(`${api['case-api'].url}/api/cases`, {
      method: 'POST',
      body: formData,
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });

    if (res.ok) {
      const data = await res.json();
      return data;
    } else {
      throw new Error('Impossibile creare il caso');
    }
  } catch (error) {
    throw new Error(error.message);
  }
};
