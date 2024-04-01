import React, { useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import { Box, Typography, Button } from '@mui/material'
import FluorescentButton from '../components/FluorescentButton'

const HomePage = () => {
    const navigate = useNavigate();

    useEffect(() => {
        if (localStorage.getItem('token') || sessionStorage.getItem('token')) navigate('/info');
    }, [navigate]);
    return (
        <Box
            sx={{
                display: 'flex',
                flexDirection: 'column'
            }}
        >
            <Box
                sx={{
                    display: 'flex',
                    flexDirection: 'column',
                    alignItems: 'center',
                    justifyContent: 'center',
                    height: 'calc(100vh - 64px)',
                }}
            >
                <Typography
                    variant='h2'
                    sx={{
                        fontWeight: 'bold',
                        fontSize: '2rem',
                        color: 'primary.main',
                        marginBottom: '40px',
                    }}
                >Welcome to Login App</Typography>
                <Typography
                    variant='h4'
                    sx={{
                        fontSize: '1.4rem',
                        marginBottom: '20px',
                        color: 'primary.main',
                        width: '60%',
                    }}
                > The Login app is a straightforward application that facilitates user login and registration, serving as the authentication component for other projects.
                </Typography>
                <Button
                    variant='contained'
                    color='primary'
                    size='large'
                    sx={{
                        fontSize: '1.25rem',
                        padding: '10px 100px',
                        borderRadius: '15px',
                    }}
                    onClick={() => navigate('/login')}
                >Get Started</Button>
                {/* <FluorescentButton color='#f44336bb' style={{ fontSize: '24px' }}>Hover This Button</FluorescentButton>
            <FluorescentButton color='#2196f3bb' style={{ fontSize: '24px' }}>Hover This Button</FluorescentButton>
            <FluorescentButton color='#4caf50bb' style={{ fontSize: '24px' }}>Hover This Button</FluorescentButton>
            <FluorescentButton color='#ffeb3bbb' style={{ fontSize: '24px' }}>Hover This Button</FluorescentButton>
            <FluorescentButton color='#ff9800bb' style={{ fontSize: '24px' }}>Hover This Button</FluorescentButton> */}
            </Box>

            <Box
                sx={{
                    display: 'flex',
                    flexDirection: 'column',
                    alignItems: 'center',
                    gap: '20px',
                    marginTop: '20px',
                    minHeight: 'calc(100vh - 64px)',
                }}
            >
                <Typography
                    variant='h3'
                    sx={{
                        color: 'primary.main',
                        marginTop: '20px',
                        marginLeft: '50px',
                        alignSelf: 'flex-start',
                    }}
                >Features</Typography>
                <Typography
                    variant='h6'
                    sx={{
                        color: 'primary.main',
                    }}
                > Lorem ipsum dolor sit amet, consectetur adipiscing elit.
                    Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.
                    Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.
                    Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.
                    Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.
                </Typography>
                <FluorescentButton color='#f44336bb' style={{ fontSize: '24px' }}>Hover This Button</FluorescentButton>
                <FluorescentButton color='#2196f3bb' style={{ fontSize: '24px' }}>Hover This Button</FluorescentButton>
                <FluorescentButton color='#4caf50bb' style={{ fontSize: '24px' }}>Hover This Button</FluorescentButton>
            </Box>
        </Box>
    )
}

export default HomePage
