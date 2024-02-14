import React from 'react'
import Navbar from './Navbar'
import { Outlet } from 'react-router-dom'
import { Container } from '@mui/material'

const Layout = () => {
    return (
        <div>
            <Navbar />
            <Container
                sx={{
                    marginTop: '1rem'
                }}
            >
                <Outlet />
            </Container>
        </div>
    )
}

export default Layout