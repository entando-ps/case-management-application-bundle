import { Route, Routes } from 'react-router-dom';

import './App.css';
import CaseEntryForm from './components/CaseEntryForm';
import WelcomeArea from './components/WelcomeArea';

function App() {
  return (
    <Routes>
      <Route path="/" element={<WelcomeArea />} />
      <Route path="/case-entry" element={<CaseEntryForm />} />
    </Routes>
  );
}

export default App;
