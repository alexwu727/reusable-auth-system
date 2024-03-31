import React from 'react'
import { NavLink } from 'react-router-dom'
import { Typography } from '@mui/material'

const NavItem = ({ to, style, children }) => {
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
                    backgroundColor: '#33333322',
                },
                padding: '3px 10px',
                border: '2px solid transparent',
                borderRadius: '12px',
                ...style
            }}
        >
            {children}
        </Typography>
    )
}

export default NavItem