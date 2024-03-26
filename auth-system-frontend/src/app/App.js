import { BrowserRouter, Routes, Route } from 'react-router-dom';
import './App.css';
import HomePage from '../pages/HomePage';
import RegisterPage from '../pages/RegisterPage';
import VerifyPage from '../pages/VerifyPage';
import VerifyResultPage from '../pages/VerifyResultPage';
import LoginPage from '../pages/LoginPage';
import ForgotPasswordPage from '../pages/ForgotPasswordPage';
import ExplorePage from '../pages/ExplorePage';
import MySpacePage from '../pages/MySpacePage';
import AdminPage from '../pages/AdminPage';
import InfoPage from '../pages/InfoPage';
import ProfilePage from '../pages/ProfilePage';
import Layout from '../components/Layout';
import UserProvider from '../contexts/UserProvider';
import PublicRoutes from '../components/PublicRoutes';
import ProtectedRoutes from '../components/ProtectedRoutes';
import NotFoundPage from '../pages/NotFoundPage';

function App() {
  return (

    <div className="App">
      <BrowserRouter>
        <UserProvider>
          <Routes>
            <Route path="/" element={<Layout />} >
              <Route element={<PublicRoutes />} >
                <Route path="/" element={<HomePage />} />
                <Route path="register" element={<RegisterPage />} />
                <Route path="verify" element={<VerifyPage />} />
                <Route path="verify-result" element={<VerifyResultPage />} />
                <Route path="login" element={<LoginPage />} />
                <Route path="forgot-password" element={<ForgotPasswordPage />} />
              </Route>
              <Route element={<ProtectedRoutes />} >
                <Route path="admin" element={<AdminPage />} />
                <Route path="info" element={<InfoPage />} />
                <Route path="profile" element={<ProfilePage />} />
                <Route path="explore" element={<ExplorePage />} />
                <Route path="my-space" element={<MySpacePage />} />
              </Route>
              <Route path="*" element={<NotFoundPage />} />
            </Route>
          </Routes>
        </UserProvider>
      </BrowserRouter>
    </div>
  );
}

export default App;
