import React, { useState } from 'react'
import { Visibility, VisibilityOff } from '@mui/icons-material';
import { Box, Typography, TextField, Button, Divider, InputAdornment, IconButton, Link as MuiLink } from '@mui/material';
import { useNavigate, Link as RouterLink } from 'react-router-dom';
import { useRegisterMutation } from '../services/apiService';


const RegisterPage = () => {
    const [register, { error }] = useRegisterMutation();
    const [user, setUser] = useState({
        username: '',
        email: '',
        password: '',
        role: 'USER'
    });
    const navigate = useNavigate();
    const [showPassword, setShowPassword] = useState(false);

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
                    label="Email"
                    name="email"
                    onChange={handleChange}
                />
                <TextField
                    required
                    label="Password"
                    type="password"
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
                        ),

                    }}
                />
                <Button
                    type="submit"
                    variant="contained"
                    sx={{ margin: '10px', borderRadius: '15px', width: '80%' }}
                >
                    Sign up
                </Button>
                {error && <Typography variant="h6" color="error">{error.data.message}</Typography>}
                <Typography
                    sx={{ fontSize: '0.75rem', margin: '10px' }}
                >Already have an account? {' '}
                    <MuiLink
                        component={RouterLink}
                        to="/login"
                        color="secondary"
                        sx={{ textDecoration: 'none' }}
                    >
                        Login
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
            </Box>
        </div>
    )
}

export default RegisterPage