import { Route, Routes } from 'react-router-dom';

import './App.css';
import CaseEntryForm from './components/CaseEntryForm';
import CaseEntrySuccess from './components/CaseEntrySuccess';
import CaseView from './components/CaseView';
import UserArea from './components/UserArea';
import { ToastProvider } from './contexts/ToastContext';

function App({ config }) {
  return (
    <ToastProvider>
      <Routes>
        <Route path="/" element={<UserArea config={config} />} />
        <Route path="/case/:id" element={<CaseView config={config} />} />
        <Route path="/case-entry" element={<CaseEntryForm config={config} />} />
        <Route path="/case-entry-success" element={<CaseEntrySuccess />} />
      </Routes>
    </ToastProvider>
  );
}

export default App;
