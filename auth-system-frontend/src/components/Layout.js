import React from 'react'
import PublicNavbar from './PublicNavbar'
import ProtectedNavbar from './ProtectedNavbar'
import { Outlet } from 'react-router-dom'
import { Container } from '@mui/material'
import useIsUserLoggedIn from '../hooks/useIsUserLoggedIn'
import { useUser } from '../contexts/UserProvider'

const Layout = () => {
    const { user, isLoading } = useUser();
    useIsUserLoggedIn();

    return (
        <div>
            <Container>
                {!isLoading && user ? <ProtectedNavbar /> : <PublicNavbar />}
                <Outlet />
            </Container>
        </div>
    )
}

export default Layout