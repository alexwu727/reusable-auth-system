import React, { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom';
import { Box, Checkbox, Typography, TextField, Button, InputAdornment, IconButton } from '@mui/material';
import { Visibility, VisibilityOff } from '@mui/icons-material';
import { useLoginMutation } from '../services/apiService';
import { useUser } from '../contexts/UserProvider';

const LoginPage = () => {
    const { setIsLoading } = useUser();
    const [loginUser, { error }] = useLoginMutation();
    const navigate = useNavigate();
    const [showPassword, setShowPassword] = useState(false);
    const [user, setUser] = useState({
        username: '',
        password: '',
        rememberMe: false
    });

    useEffect(() => {
        var token = localStorage.getItem('token') || sessionStorage.getItem('token');
        // if (token) navigate('/info');
    }, [navigate]);

    const handleChange = (e) => {
        setUser({
            ...user,
            [e.target.name]: e.target.value
        });
    }

    const handleRememberMe = (e) => {
        setUser({
            ...user,
            rememberMe: e.target.checked
        });
    }

    const handleSubmit = (e) => {
        e.preventDefault();
        loginUser(user)
            .unwrap()
            .then(response => {
                if (user.rememberMe) localStorage.setItem('token', response.token);
                else sessionStorage.setItem('token', response.token);
                setIsLoading(true);
                navigate("/info");
            })
            .catch(error => {
                console.log(error);
            });
    }

    const handleForgotPassword = () => {
        navigate("/forgot-password");
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
                sx={{ margin: '10px', width: '60%' }}
            />
            <TextField
                required
                label="Password"
                type={showPassword ? 'text' : 'password'}
                name="password"
                onChange={handleChange}
                sx={{ margin: '10px', width: '60%' }}
                InputProps={{
                    endAdornment: (
                        <InputAdornment position="end">
                            <IconButton
                                aria-label='toggle password visibility'
                                onClick={() => setShowPassword(!showPassword)}
                                onMouseDown={() => setShowPassword(!showPassword)}
                            >
                                {showPassword ? <Visibility /> : <VisibilityOff />}
                            </IconButton>
                        </InputAdornment>
                    )

                }}
            />
            <Box
                sx={{
                    display: 'flex',
                    flexDirection: 'row',
                    alignItems: 'center',
                    margin: '10px'
                }}
            >
                <Checkbox onChange={handleRememberMe} />
                <Typography variant="body2">Remember me</Typography>

                <Typography
                    variant="body2"
                    color="primary"
                    sx={{ margin: '10px', cursor: 'pointer', textDecoration: 'underline' }}
                    onClick={handleForgotPassword}
                >
                    Forgot password?
                </Typography>
            </Box>
            {error && error.status !== 403 && < Typography variant="h6" color="error">{error.data.message}</Typography>}
            <Button
                type="submit"
                variant="contained"
                sx={{ margin: '10px' }}
            >
                Login
            </Button>
            <Box
                sx={{
                    display: 'flex',
                    flexDirection: 'row',
                    alignItems: 'center'
                }}
            >
                <Typography variant="body2">
                    Don't have an account?
                </Typography>
                <Typography
                    variant="body2"
                    color="primary"
                    sx={{ cursor: 'pointer', textDecoration: 'underline' }}
                    onClick={() => navigate("/register")}
                >
                    Register
                </Typography>
            </Box>
        </Box >
    )
}

export default LoginPage