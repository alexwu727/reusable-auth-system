import React from 'react'
import NavItem from './NavItem'
import { Box, Typography } from '@mui/material'
import { useUser } from '../contexts/UserProvider'
import UserAvatar from './UserAvatar'

const Navbar = () => {
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
            {user && <NavItem to='/my-space'>MySpace</NavItem>}
            {user && <NavItem to='/explore'>Explore</NavItem>}
            {user && <NavItem to='/info'>Info</NavItem>}
            {user && <UserAvatar />}
            {!user && <NavItem to='/register'>Register</NavItem>}
            {!user && <NavItem to='/login'>Log in</NavItem>}
        </Box>
    )
}

export default Navbar