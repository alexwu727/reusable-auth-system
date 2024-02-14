import { Avatar } from '@mui/material'
import React, { useState } from 'react'
import { useUser } from '../UserProvider'
import { Box, Menu, MenuItem } from '@mui/material'
import { useNavigate } from 'react-router-dom'

const UserAvatar = () => {
    const navigate = useNavigate();
    const { user, updateUser } = useUser();
    const [anchorEl, setAnchorEl] = useState(null);
    const handleClick = (event) => {
        setAnchorEl(event.currentTarget);
    }
    const handleClose = () => {
        setAnchorEl(null);
    }
    const handleLogout = () => {
        localStorage.removeItem('token');
        updateUser(null);
        navigate('/login');
        // handleClose();
    }
    const handleProfile = () => {
        navigate('/profile');
        handleClose();
    }

    return (
        <Box>
            <Avatar
                onClick={handleClick}
                sx={{ cursor: 'pointer' }}
            >
                {user.username.charAt(0).toUpperCase()}
            </Avatar>
            <Menu
                anchorEl={anchorEl}
                open={Boolean(anchorEl)}
                onClose={handleClose}
            >
                <MenuItem onClick={handleProfile}>Profile</MenuItem>
                <MenuItem onClick={handleLogout}>Logout</MenuItem>
            </Menu>
        </Box>
    )
}

export default UserAvatar