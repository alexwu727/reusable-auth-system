import React, { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom';
import { Box, Typography } from '@mui/material';
import { useVerifyMutation } from '../state/apiService';
import { useSearchParams } from 'react-router-dom';

const VerifyResult = () => {
    const [verify, { error }] = useVerifyMutation();
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
        verify(code)
            .unwrap()
            .then(response => {
                setResult(response);
                setTimeout(() => {
                    navigate("/login");
                }, 5000);
            })
            .catch(e => {
                if (e.data.message === "User is already verified") {
                    setTimeout(() => {
                        navigate("/login");
                    }, 3000);
                }
            });
    }, [code, email, navigate, verify]);

    return (
        <Box
            sx={{
                display: 'flex',
                flexDirection: 'column',
                alignItems: 'center',
                margin: '40px auto',
                padding: '20px',
                width: '40%'
            }}
        >
            <Typography variant="h6" sx={{ margin: '10px' }}>{result}</Typography>
            {error && <Typography sx={{ color: 'red' }}>{error.data.message}</Typography>}
            {(result === "User verified successfully" || (error && error.data.message === "User is already verified")) && <Typography sx={{ margin: '10px' }}>Redirecting to login...</Typography>}
        </Box>
    );
}

export default VerifyResult