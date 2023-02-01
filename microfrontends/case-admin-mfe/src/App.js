import { Route, Routes } from 'react-router-dom';

import CaseView from './components/CaseView';
import Dashboard from './components/Dashboard';
import { ToastProvider } from './contexts/ToastContext';

function App({ config }) {
  return (
    <ToastProvider>
      <Routes>
        <Route path="/" element={<Dashboard config={config} />} />
        <Route path="/case/:id" element={<CaseView config={config} />} />
      </Routes>
    </ToastProvider>
  );
}

export default App;
