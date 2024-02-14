import React, { useState } from 'react'
import axios from 'axios'

const AxiosTest = () => {
    // call api and display data
    const [data1, setData1] = useState(null);
    const [data2, setData2] = useState(null);
    const [error1, setError1] = useState(null);
    const [error2, setError2] = useState(null);

    const url1 = 'http://localhost:8080/helloReactiveGateway';
    const url2 = 'http://localhost:8080/api/v1/users/';

    // fetch with jwt token
    const token = localStorage.getItem('token');
    const config = {
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        }
    }
    const handleFetch1 = async () => {
        axios.get(url2, config)
            .then(response => {
                setData1(response.data);
            })
            .catch(error => {
                setError1(error);
                console.log(config);
            });
    }

    const handleFetch2 = async () => {
        axios.post(url2, { username: 'admin', password: 'admin', test: 'test' })
            .then(response => {
                setData2(response.data);
            })
            .catch(error => {
                setError2(error);
            });
    }

    const handleClear = () => {
        setData1(null);
        setData2(null);
        setError1(null);
        setError2(null);
    }
    return (
        <div>
            <button onClick={handleFetch1}>Fetch 1</button>
            <div>
                {error1 && <p>Error: {error1.message}</p>}
                {/* display user list */}
                {data1 && <ul>{data1.map(user => <li key={user.id}>{user.username}</li>)}</ul>}
            </div>
            <button onClick={handleFetch2}>Fetch 2</button>
            <div>
                {error2 && <p>Error: {error2.message}</p>}
                {data2 && <p>{data2}</p>}
            </div>
            <button onClick={handleClear}>Clear</button>
        </div>
    )
}

export default AxiosTest