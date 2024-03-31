import React from 'react'
import NavItem from './NavItem'
import { Box, Typography } from '@mui/material'
import { useUser } from '../contexts/UserProvider'

const PublicNavbar = () => {
    return (
        <Box
            sx={{
                display: 'flex',
                alignItems: 'center',
                padding: '20px',
                color: 'primary.main',
                gap: '20px',
            }}
        >
            <NavItem to='/'>Login App</NavItem>
            <NavItem to='/register' style={{ marginLeft: 'auto' }}>Register</NavItem>
            <NavItem to='/login' style={{ border: '2px solid', padding: '3px 10px', borderRadius: '12px' }}>Login</NavItem>
        </Box>
    )
}

export default PublicNavbar