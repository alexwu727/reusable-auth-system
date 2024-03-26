import React, { useEffect, useState, useRef, useCallback } from 'react'
import { useNavigate } from 'react-router-dom';
import { Box, Typography, TextField, Button } from '@mui/material';
import { useVerifyMutation, useResendVerificationCodeMutation } from '../services/apiService';


const VerifyPage = () => {
    const [verify, { data, error }] = useVerifyMutation();
    const [resend, { data: resendData, error: resendError }] = useResendVerificationCodeMutation();
    const navigate = useNavigate();
    const [code, setCode] = useState('');
    const inputRef = [
        useRef(),
        useRef(),
        useRef(),
        useRef(),
        useRef(),
        useRef()
    ]

    const resetCode = () => {
        setCode('');
        inputRef.forEach(ref => {
            ref.current.value = '';
        });
        inputRef[0].current.focus();
    }

    const handleInput = (e, index) => {
        const value = e.target.value;
        const next = inputRef[index + 1];

        const newCode = [...code];

        if (isNaN(value) || value === ' ') {
            e.target.value = '';
            return;
        }
        newCode[index] = value;
        setCode(newCode.join(''));

        e.target.select();

        if (value && next) {
            next.current.select();
        }
    }

    const handleKeyDown = (e, index) => {
        if (e.key === 'Backspace' && index > 0 && e.target.value === '') {
            const prev = inputRef[index - 1];
            prev.current.focus();
        }
    }

    const handleFocus = (e, index) => {
        const prev = inputRef[index - 1];
        if (index > 0 && !prev.current.value) {
            prev.current.focus();
        }
    }

    const handlePaste = (e, currIndex) => {
        e.preventDefault();
        var text = e.clipboardData.getData('Text');
        const length = text.length;
        if (length > 6 - currIndex) {
            text = text.slice(0, 6 - currIndex);
        }
        console.log(text);
        inputRef.forEach((ref, index) => {
            if (index < currIndex) {
                ref.current.value = code[index];
            } else if (index >= currIndex && index < currIndex + length) {
                ref.current.value = text[index - currIndex];
            } else {
                ref.current.value = '';
            }
        });
        setCode(text);
        inputRef[Math.min(currIndex + length, 5)].current.focus();
    }

    const handleVerify = useCallback(() => {
        verify(code)
            .unwrap()
            .then(response => {
                setTimeout(() => {
                    navigate("/login");
                }, 3000);
            })
            .catch(e => {
                if (e.data?.message === "User is already verified") {
                    setTimeout(() => {
                        navigate("/login");
                    }, 3000);
                }
            });
    }, [code, navigate, verify]);

    const handleResend = () => {
        resend()
            .unwrap()
            .then(response => {
                console.log(response);
            })
            .catch(e => {
                console.log(e);
            });
    }

    const handleSubmit = (e) => {
        e.preventDefault();
        handleVerify();
    }

    useEffect(() => {
        const email = localStorage.getItem('email');
        if (!email) {
            navigate('/register');
        }
    }, [navigate]);

    useEffect(() => {
        if (code.length === 6) {
            handleVerify();
        }
    }, [code, handleVerify]);

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
            <Typography variant="h4" sx={{ margin: '10px' }}>Verify</Typography>
            <Box sx={{ display: 'flex', justifyContent: 'center' }}>
                {Array.from({ length: 6 }).map((_, index) => (
                    <TextField
                        key={index}
                        inputRef={inputRef[index]}
                        autoFocus={index === 0}
                        autoComplete="off"
                        name="code"
                        onFocus={(e) => handleFocus(e, index)}
                        onKeyDown={(e) => handleKeyDown(e, index)}
                        inputProps={{
                            maxLength: 1,
                            onInput: (e) => handleInput(e, index),
                            onPaste: (e) => handlePaste(e, index),
                            style: { textAlign: 'center', fontSize: '18px' }
                        }}
                        sx={{
                            margin: '10px',
                            width: '50px'
                        }}
                    />
                ))}
            </Box>
            <Button
                variant="contained"
                onClick={resetCode}
                sx={{ margin: '10px' }}
            >
                Reset
            </Button>
            <Button
                type="submit"
                variant="contained"
                sx={{ margin: '10px' }}
            >
                Verify
            </Button>
            <Button
                variant="contained"
                onClick={handleResend}
                sx={{ margin: '10px' }}
            >
                Resend
            </Button>
            {error && code.length === 6 && <Typography sx={{ color: 'red' }}>{error.data.message}</Typography>}
            {data && <Typography sx={{ margin: '10px' }}>{data.message}</Typography>}
            {((data && data.message === "User verified successfully") || (error && error.data.message === "User is already verified")) && <Typography sx={{ margin: '10px' }}>Redirecting to login...</Typography>}
            {resendError && <Typography sx={{ color: 'red' }}>{resendError.data.message}</Typography>}
            {resendData && <Typography sx={{ margin: '10px' }}>{resendData.message}</Typography>}
        </Box>
    )
}

export default VerifyPage