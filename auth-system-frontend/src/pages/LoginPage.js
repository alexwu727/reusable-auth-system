import React, { useState, useEffect } from 'react'
import { useNavigate, Link as RouterLink } from 'react-router-dom';
import { Box, Checkbox, Typography, TextField, Button, InputAdornment, IconButton, Divider, Link as MuiLink } from '@mui/material';
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
        <div>
            <Typography sx={{
                fontSize: '1.75rem',
                textAlign: 'center',
                fontWeight: 'bold',
            }}>Sign up</Typography>
            <Box
                component="form"
                onSubmit={handleSubmit}
                sx={{
                    background: '#f3f3f3',
                    display: 'flex',
                    flexDirection: 'column',
                    alignItems: 'center',
                    margin: '20px auto',
                    padding: '20px',
                    borderRadius: '15px',
                    width: '25%',
                    boxShadow: 2,
                    justifyContent: 'center'
                }}
            >
                <TextField
                    required
                    label="Username"
                    name="username"
                    onChange={handleChange}
                />
                <TextField
                    required
                    label="Password"
                    type={showPassword ? 'text' : 'password'}
                    name="password"
                    onChange={handleChange}
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
                <Box sx={{
                    display: 'flex',
                    alignItems: 'center',
                    alignSelf: 'flex-start',
                    marginLeft: '6.5%'
                }}>
                    <Checkbox size="small" onChange={handleRememberMe} />
                    <Typography sx={{ fontSize: '0.8rem' }}>Remember me</Typography>
                </Box>
                <Typography
                    variant="body2"
                    color="primary"
                    sx={{ cursor: 'pointer', textDecoration: 'underline', margin: '1px' }}
                    onClick={handleForgotPassword}
                >
                    Forgot password?
                </Typography>
                {error && error.status !== 403 && < Typography variant="h6" color="error">{error.data.message}</Typography>}
                <Button
                    type="submit"
                    variant="contained"
                    sx={{ margin: '10px', width: '80%', borderRadius: '15px' }}
                >
                    Login
                </Button>
                <Typography
                    sx={{ fontSize: '0.75rem', margin: '10px' }}
                >Don't have an account? {' '}
                    <MuiLink
                        component={RouterLink}
                        to="/register"
                        color="secondary"
                        sx={{ textDecoration: 'none' }}
                    >
                        Sign up
                    </MuiLink>
                </Typography>
                <Divider sx={{ margin: '10px', width: '80%' }} > or </Divider>
                <Button
                    variant="outlined"
                    sx={{ margin: '10px', width: '80%', borderRadius: '15px' }}
                > Sign in with Google</Button>
                <Button
                    variant="outlined"
                    sx={{ margin: '10px', width: '80%', borderRadius: '15px' }}
                > Sign in with GitHub</Button>

            </Box >
        </div>
    )
}

export default LoginPage