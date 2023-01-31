import { Route, Routes } from 'react-router-dom';

import Dashboard from './components/Dashboard';

function App({ config }) {
  return (
    <Routes>
      <Route path="/" element={<Dashboard config={config} />} />
      {/* <Route path="/case/:id" element={<CaseView config={config} />} /> */}
    </Routes>
  );
}

export default App;
