import React from 'react'
import NavItem from './NavItem'
import { Box, Typography } from '@mui/material'
import UserAvatar from './UserAvatar'
import { useUser } from '../contexts/UserProvider'

const ProtectedNavbar = () => {
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

            <Typography
                sx={{
                    fontWeight: 'bold',
                    marginLeft: '20px',
                    marginRight: '50%'
                }}
            >Login App</Typography>
            {user && user.role === 'ADMIN' && <NavItem to='/admin'>Admin</NavItem>}
            <NavItem to='/my-space'>MySpace</NavItem>
            <NavItem to='/explore'>Explore</NavItem>
            <NavItem to='/info'>Info</NavItem>
            <UserAvatar />
        </Box>
    )
}

export default ProtectedNavbar