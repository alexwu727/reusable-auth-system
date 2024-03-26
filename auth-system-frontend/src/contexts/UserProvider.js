import React, { createContext, useContext, useState } from 'react';
import { useNavigate } from 'react-router-dom';

const UserContext = createContext();

export const useUser = () => {
    return useContext(UserContext);
}

const UserProvider = ({ children }) => {
    const [user, setUser] = useState(null);
    const [isLoading, setIsLoading] = useState(true);
    const navigate = useNavigate();

    const login = (data) => {
        setUser(data);
    }

    const logout = () => {
        setUser(null);
    }

    return (
        <UserContext.Provider value={{ user, login, logout, isLoading, setIsLoading }}>
            {children}
        </UserContext.Provider>
    )
}

export default UserProvider;