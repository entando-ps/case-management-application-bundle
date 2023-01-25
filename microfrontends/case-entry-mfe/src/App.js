import { Route, Routes } from 'react-router-dom';

import './App.css';
import CaseEntryForm from './components/CaseEntryForm';
import CaseEntrySuccess from './components/CaseEntrySuccess';
import CaseView from './components/CaseView';
import UserArea from './components/UserArea';

function App({ config }) {
  return (
    <Routes>
      <Route path="/" element={<UserArea config={config} />} />
      <Route path="/case-view" element={<CaseView />} />
      <Route path="/case-entry" element={<CaseEntryForm config={config} />} />
      <Route path="/case-entry-success" element={<CaseEntrySuccess />} />
    </Routes>
  );
}

export default App;
