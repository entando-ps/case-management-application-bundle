import { Route, Routes } from 'react-router-dom';

import './App.css';
import CaseEntryForm from './components/CaseEntryForm';
import CaseEntrySuccess from './components/CaseEntrySuccess';
import WelcomeArea from './components/WelcomeArea';


function App() {
  return (
    <Routes>
      <Route path="/" element={<WelcomeArea />} />
      <Route path="/case-entry" element={<CaseEntryForm />} />
      <Route path="/case-entry-success" element={<CaseEntrySuccess />} />
    </Routes>
  );
}

export default App;
