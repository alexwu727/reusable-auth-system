import React, { useState } from 'react'
import axios from 'axios'
import { useNavigate } from 'react-router-dom';
import { Box, Typography, TextField, Button } from '@mui/material';

const LoginForm = () => {
    const navigate = useNavigate();
    const [error, setError] = useState(null);
    const [user, setUser] = useState({
        username: '',
        password: ''
    });

    const handleChange = (e) => {
        setUser({
            ...user,
            [e.target.name]: e.target.value
        });
    }

    const handleSubmit = (e) => {
        e.preventDefault();
        axios.post('http://localhost:8080/api/v1/auth/login', user)
            .then(response => {
                localStorage.setItem('token', response.data.token);
                navigate("/info");
            })
            .catch(error => {
                setError("Invalid username or password");
            });
    }

    return (
        <Box
            component="form"
            onSubmit={handleSubmit}
            sx={{
                display: 'flex',
                flexDirection: 'column',
                alignItems: 'center',
                margin: '40px auto',
                padding: '20px',
                border: '3px solid',
                borderColor: 'primary.main',
                borderRadius: '15px',
                width: '40%'
            }}
        >
            <Typography variant="h4" sx={{ margin: '10px' }}>Login</Typography>
            <TextField
                required
                label="Username"
                name="username"
                onChange={handleChange}
                sx={{ margin: '10px' }}
            />
            <TextField
                required
                label="Password"
                type="password"
                name="password"
                onChange={handleChange}
                sx={{ margin: '10px' }}
            />
            {/* error */}
            {error && <Typography variant="h6" color="error">{error}</Typography>}
            <Button
                type="submit"
                variant="contained"
                sx={{ margin: '10px' }}
            >
                Login
            </Button>
        </Box>
    )
}

export default LoginForm