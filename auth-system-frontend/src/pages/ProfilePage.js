import React from 'react'
import { useUser } from '../contexts/UserProvider'
import { Box, List, ListItem, ListItemText, Divider } from '@mui/material'

const ProfilePage = () => {
    const { user } = useUser();

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

export default ProfilePage