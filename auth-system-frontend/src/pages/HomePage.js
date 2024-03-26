import React, { useEffect } from 'react'
import { useNavigate } from 'react-router-dom'

const HomePage = () => {
    const navigate = useNavigate();

    useEffect(() => {
        if (localStorage.getItem('token') || sessionStorage.getItem('token')) navigate('/info');
    }, [navigate]);
    return (
        <div>Home</div>
    )
}

export default HomePage