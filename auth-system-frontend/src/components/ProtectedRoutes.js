import React, { useEffect } from 'react'
import { Outlet, useNavigate } from 'react-router-dom'
import { useUser } from '../contexts/UserProvider'

const ProtectedRoutes = () => {
    const navigate = useNavigate();
    const { user, isLoading } = useUser();

    useEffect(() => {
        if (!isLoading && !user) {
            navigate('/login');
        }
    }, [user, navigate]);

    if (isLoading) return <div>Loading...</div>;

    return <Outlet />
}

export default ProtectedRoutes