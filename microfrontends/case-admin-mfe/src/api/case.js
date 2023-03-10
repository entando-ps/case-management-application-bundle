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

export const getCase = async (id, config, token) => {
  const { api } = config.systemParams;

  try {
    const res = await fetch(`${api['case-api'].url}/api/cases/${id}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });

    if (res.ok) {
      const data = await res.json();
      return data;
    } else {
      throw new Error('Impossibile recuperare il caso');
    }
  } catch (error) {
    throw new Error(error.message);
  }
};

export const postCase = async (data, config, token) => {
  const { api } = config.systemParams;
  const { documents, authorized, subscriber } = data;

  const formData = new FormData();
  documents.forEach(document => {
    formData.append('attachments', document?.[0]);
  });
  formData.append(
    'case_metadata',
    new Blob(
      [JSON.stringify({ authorized, subscriber })],
      { type: 'application/json' }
    ),
  );
  
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

export const rejectCase = async (id, config, token) => {
  const { api } = config.systemParams;

  try {
    const res = await fetch(`${api['case-api'].url}/api/cases/${id}/reject`, {
      method: 'POST',
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });

    if (!res.ok) {
      throw new Error('Impossibile rifiutare il caso');
    }
  } catch (error) {
    throw new Error(error.message);
  }
};

export const approveCase = async (id, config, token) => {
  const { api } = config.systemParams;
  
  try {
    const res = await fetch(`${api['case-api'].url}/api/cases/${id}/approve`, {
      method: 'POST',
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });

    if (!res.ok) {
      throw new Error('Impossibile approvare il caso');
    }
  } catch (error) {
    throw new Error(error.message);
  }
};

export const deleteCase = async (id, config, token) => {
  const { api } = config.systemParams;
  
  try {
    const res = await fetch(`${api['case-api'].url}/api/cases/${id}`, {
      method: 'DELETE',
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });

    if (!res.ok) {
      throw new Error('Impossibile eliminare il caso');
    }
  } catch (error) {
    throw new Error(error.message);
  }
};
