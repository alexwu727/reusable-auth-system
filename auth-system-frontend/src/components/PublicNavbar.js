import React from 'react'
import NavItem from './NavItem'
import { Box, Typography } from '@mui/material'
import { useUser } from '../contexts/UserProvider'

const PublicNavbar = () => {
    const { user } = useUser();
    return (
        <Box
            sx={{
                display: 'flex',
                justifyContent: 'space-between',
                alignItems: 'center',
                padding: '20px',
                color: 'primary.main',
                borderBottom: '2px solid',
            }}
        >

            <NavItem to='/'>
                <Typography
                    sx={{
                        fontWeight: 'bold',
                        marginLeft: '20px',
                        marginRight: '50%'
                    }}
                >Login App</Typography>
            </NavItem>
            <NavItem to='/register'>Register</NavItem>
            <NavItem to='/login'>Log in</NavItem>
        </Box>
    )
}

export default PublicNavbar