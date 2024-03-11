import React, { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import axios from 'axios'
import { useUser } from '../UserProvider'
import { useGetCurrentUserQuery } from '../state/apiService'

const Info = () => {
    const { data: user, error, isLoading } = useGetCurrentUserQuery();
    const [data, setData] = useState('');
    const navigate = useNavigate();
    const { updateUser } = useUser();

    const handleLogout = () => {
        localStorage.removeItem('token');
        updateUser(null);
        navigate('/login');
    }
    const token = localStorage.getItem('token');
    const config = {
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        }
    }
    const fetch = () => {
        axios.get('http://localhost:8080/api/v1/users/info', config)
            .then(response => {
                updateUser(response.data);
                setData(response.data);
            })
            .catch(error => {
                console.log(error);
            });
    }
    useEffect(() => {
        if (!token) navigate('/login');
        fetch();
    }, []);
    return (
        <div>
            <div className='bg'> {data.username} </div>
            <h1>Info</h1>
            <p>{data.username}</p>
            <p>{data.email}</p>
            <p>{data.role}</p>
            <button onClick={handleLogout}>Logout</button>
        </div>
    )
}

export default Info