// useIsUserLoggedIn.js
import { useEffect, useState } from 'react';
import { useUser } from "../contexts/UserProvider";
import { useLazyGetCurrentUserQuery } from "../services/apiService";

const useIsUserLoggedIn = () => {
    const { user, login, setIsLoading } = useUser();
    const [trigger] = useLazyGetCurrentUserQuery();
    const [isUserLoggedIn, setIsUserLoggedIn] = useState(null);

    useEffect(() => {
        setIsLoading(true);

        if (user) {
            setIsUserLoggedIn(true);
            setIsLoading(false);
            return;
        }

        const token = localStorage.getItem('token') || sessionStorage.getItem('token');

        if (!token) {
            setIsUserLoggedIn(false);
            setIsLoading(false);
            return;
        }

        trigger()
            .unwrap()
            .then(response => {
                login(response);
                setIsUserLoggedIn(true);
                setIsLoading(false);
            })
            .catch(error => {
                console.log(error);
                setIsUserLoggedIn(false);
                setIsLoading(false);
            });
    }, [user, trigger, login]);

    return { isUserLoggedIn };
}

export default useIsUserLoggedIn;
