import { BrowserRouter, Routes, Route } from 'react-router-dom';
import './App.css';
import RegisterForm from './components/RegisterForm';
import Verify from './components/Verify';
import VerifyResult from './components/VerifyResult';
import LoginForm from './components/LoginForm';
import Explore from './components/Explore';
import MySpace from './components/MySpace';
import Admin from './components/Admin';
import Info from './components/Info';
import Profile from './components/Profile';
import Layout from './components/Layout';
import UserProvider from './UserProvider';
import { apiService } from './state/apiService';

function App() {
  return (
    <UserProvider>
      <div className="App">
        <BrowserRouter>
          <Routes>
            <Route path="/" element={<Layout />} >
              <Route path="register" element={<RegisterForm />} />
              <Route path="verify" element={<Verify />} />
              <Route path="verify-result" element={<VerifyResult />} />
              <Route path="login" element={<LoginForm />} />
              <Route path="admin" element={<Admin />} />
              <Route path="info" element={<Info />} />
              <Route path="profile" element={<Profile />} />
              <Route path="explore" element={<Explore />} />
              <Route path="my-space" element={<MySpace />} />
            </Route>
          </Routes>
        </BrowserRouter>
      </div>
    </UserProvider>
  );
}

export default App;
