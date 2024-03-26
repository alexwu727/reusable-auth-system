import React, { useEffect } from 'react'
import Navbar from './Navbar'
import PublicNavbar from './PublicNavbar'
import { Outlet } from 'react-router-dom'
import { Container } from '@mui/material'
import useIsUserLoggedIn from '../hooks/useIsUserLoggedIn'
import { useUser } from '../contexts/UserProvider'

const Layout = () => {
    const { user, isLoading } = useUser();
    const { isUserLoggedIn } = useIsUserLoggedIn();

    return (
        <div>
            {isUserLoggedIn ? <Navbar /> : <PublicNavbar />}
            <Container sx={{ marginTop: '1rem' }}>
                <Outlet />
            </Container>
        </div>
    )
}

export default Layout