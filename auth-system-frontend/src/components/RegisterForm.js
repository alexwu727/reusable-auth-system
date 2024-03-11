import React, { useState } from 'react'
// import axios from 'axios'
import { Box, Typography, TextField, Button } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { useRegisterMutation } from '../state/apiService';

const RegisterForm = () => {
    const [register, { error }] = useRegisterMutation();
    const [user, setUser] = useState({
        username: '',
        email: '',
        password: '',
        role: 'USER'
    });
    const navigate = useNavigate();

    const handleChange = (e) => {
        setUser({
            ...user,
            [e.target.name]: e.target.value
        });
    }

    const handleSubmit = (e) => {
        e.preventDefault();
        register(user)
            .unwrap()
            .then(response => {
                localStorage.setItem('email', user.email);
                navigate("/verify");
            })
            .catch(error => {
                console.log(error);
            });
    }

    // const handleSubmit = (e) => {
    //     e.preventDefault();
    //     axios.post('http://localhost:8080/api/v1/auth/register', user)
    //         .then(response => {
    //             localStorage.setItem('token', response.data.token);
    //             navigate("/info");
    //         })
    //         .catch(error => {
    //             if (error.response) {
    //                 setError(error.response.data.message)
    //             }
    //         });
    // }

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
            <Typography variant="h4" sx={{ margin: '10px' }}>Register</Typography>
            <TextField
                required
                label="Username"
                name="username"
                onChange={handleChange}
                sx={{ margin: '10px' }}
            />
            <TextField
                required
                label="Email"
                name="email"
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
            <Button
                type="submit"
                variant="contained"
                sx={{ margin: '10px' }}
            >
                Register
            </Button>
            {error && <Typography variant="h6" color="error">{error.data.message}</Typography>}
        </Box>
    )
}

export default RegisterForm