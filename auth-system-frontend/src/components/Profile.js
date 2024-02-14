import React, { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import axios from 'axios'
import { useUser } from '../UserProvider'
import { Box, List, ListItem, ListItemButton, ListItemText, ListItemIcon, Divider } from '@mui/material'

const Profile = () => {
    const { user, updateUser } = useUser();
    const navigate = useNavigate();
    const token = localStorage.getItem('token');
    const config = {
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        }
    }
    const fetch = () => {
        axios.get('http://localhost:8080/api/v1/users/info', config)
            .then(response => {
                updateUser(response.data);
            })
            .catch(error => {
                console.log(error);
            });
    }
    useEffect(() => {
        if (!token) navigate('/login');
        if (!user) fetch();
    }, []);


    return (
        <Box>
            <h1>Profile</h1>
            <List>
                <ListItem>
                    <ListItemText primary="Username" secondary={user.username} />
                </ListItem>
                <Divider />
                <ListItem>
                    <ListItemText primary="Email" secondary={user.email} />
                </ListItem>
                <Divider />
                <ListItem>
                    <ListItemText primary="Role" secondary={user.role} />
                </ListItem>
            </List>
        </Box>
    )
}

export default Profile