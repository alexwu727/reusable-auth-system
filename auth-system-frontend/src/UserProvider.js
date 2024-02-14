import React, { createContext, useContext, useState } from 'react';

const UserContext = createContext();

export const useUser = () => {
    return useContext(UserContext);
}

const UserProvider = ({ children }) => {
    const [user, setUser] = useState(null);

    const updateUser = (data) => {
        setUser(data);
    }

    return (
        <UserContext.Provider value={{ user, updateUser }}>
            {children}
        </UserContext.Provider>
    )
}

export default UserProvider;