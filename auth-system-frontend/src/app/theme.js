import { createTheme } from "@mui/material";

const theme = createTheme({
    palette: {
        primary: {
            main: '#f44336',
        },
        secondary: {
            main: '#3f51b5',
        },
    },
    typography: {
        fontFamily: 'Reddit Mono, monospace',
    },
    components: {
        MuiButton: {
            styleOverrides: {
                root: {
                    textTransform: 'none',
                },
            },
        },
        MuiCssBaseline: {
            styleOverrides: `
                body {
                    font-family: 'Reddit Mono', monospace;
                }
            `,
        },
        MuiFormControl: {
            styleOverrides: {
                root: {
                    margin: '10px 0',
                    width: '80%',
                },
            },
        },
        MuiOutlinedInput: {
            styleOverrides: {
                root: {
                    borderRadius: '15px',
                    height: '40px'
                },
            },
        },
        MuiInputLabel: {
            styleOverrides: {
                root: {
                    color: '#f44336',
                    top: '50%',
                    transform: 'translate(14px, -50%)',
                    transformOrigin: 'top left',
                    '&.MuiInputLabel-shrink': {
                        transform: 'translate(14px, -115%) scale(0.75)',
                    }
                },
            },
        },
    },
});

export default theme;