import React from 'react'
import { useNavigate } from 'react-router-dom'
import { useUser } from '../contexts/UserProvider'

const InfoPage = () => {
    const navigate = useNavigate();
    const { user, logout } = useUser();

    const handleLogout = () => {
        localStorage.removeItem('token');
        sessionStorage.removeItem('token');
        logout();
        navigate('/login');
    }

    return (
        <div>
            <h1>Welcome {user?.name}</h1>
            <h2>Email: {user?.email}</h2>
            <button onClick={handleLogout}>Logout</button>
        </div>
    )
}

export default InfoPage