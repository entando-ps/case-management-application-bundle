import { Route, Routes } from 'react-router-dom';

import './App.css';
import CaseEntryForm from './components/CaseEntryForm';
import CaseEntrySuccess from './components/CaseEntrySuccess';
import UserArea from './components/UserArea';

function App() {
  return (
    <Routes>
      <Route path="/" element={<UserArea />} />
      <Route path="/case-entry" element={<CaseEntryForm />} />
      <Route path="/case-entry-success" element={<CaseEntrySuccess />} />
    </Routes>
  );
}

export default App;
