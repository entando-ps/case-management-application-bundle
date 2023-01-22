import { Route, Routes } from 'react-router-dom';

import './App.css';
import WelcomeArea from './components/WelcomeArea';

function App() {
  return (
    <Routes>
      <Route path="/" element={<WelcomeArea />} />
    </Routes>
  );
}

export default App;
