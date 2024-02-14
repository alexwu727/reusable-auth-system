import React from 'react'
import { NavLink } from 'react-router-dom'
import { Typography } from '@mui/material'

const NavItem = ({ to, children }) => {
    return (
        <Typography
            component={NavLink}
            to={to}
            sx={{
                textDecoration: 'none',
                color: 'inherit',
                '&.active': {
                    color: 'primary.secondary',
                    fontWeight: 'bold'
                },
                '&:hover': {
                    color: 'primary.secondary',
                }

            }}
        >
            {children}
        </Typography>
    )
}

export default NavItem