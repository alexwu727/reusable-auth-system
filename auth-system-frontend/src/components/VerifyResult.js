import React, { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom';
import { Box, Typography, TextField, Button } from '@mui/material';
import { useLazyVerifyQuery } from '../state/apiService';
import { useSearchParams } from 'react-router-dom';

const VerifyResult = () => {
    const [trigger, { data: message, isLoading, error }] = useLazyVerifyQuery();
    const [searchParams] = useSearchParams();
    const code = searchParams.get('code');
    const email = searchParams.get('email');
    const navigate = useNavigate();
    const [result, setResult] = useState('');

    useEffect(() => {
        if (!code || !email) {
            console.log("No code or email");
            navigate('/login');
        }
        localStorage.setItem('email', email);
        trigger(code)
            .then(response => {
                if (!response.error) {
                    console.log(response);
                    setResult(response);
                    const timer = setTimeout(() => {
                        navigate("/login");
                    }, 3000);
                } else {
                    console.log(response.error);
                    console.log("--------------------")
                    console.log(error);
                }
            })
            .catch(e => {
                console.log("--------------------")
                console.log(e);
            })
    }, []);

    return (
        <Box
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
            <Typography variant="h6" sx={{ margin: '10px' }}>{result}</Typography>
            {isLoading && <Typography sx={{ margin: '10px' }}>Loading...</Typography>}
            {error && <Typography sx={{ color: 'red' }}>{error.data.message}</Typography>}
            {message && <Typography sx={{ margin: '10px' }}>{message}</Typography>}
            {message === "User verified successfully" && <Typography sx={{ margin: '10px' }}>Redirecting to login...</Typography>}
        </Box>
    );
}

export default VerifyResult