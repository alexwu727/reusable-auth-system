import React, { useEffect, useState } from 'react'
import axios from 'axios'
import { DataGrid } from '@mui/x-data-grid';

const AdminPage = () => {
    const columns = [
        { field: 'id', headerName: 'ID', width: 70 },
        { field: 'username', headerName: 'Username', flex: 1, minWidth: 150 },
        { field: 'email', headerName: 'Email', flex: 1.5, minWidth: 200 },
        { field: 'role', headerName: 'Role', flex: 0.3, minWidth: 100 },
    ];
    const [users, setUsers] = useState([]);
    const token = localStorage.getItem('token');
    const config = {
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        }
    }
    useEffect(() => {
        axios.get('http://localhost:8080/api/v1/users/', config)
            .then(response => {
                setUsers(response.data);
            })
    }, []);

    return (
        <div style={{ height: 400, width: '100%' }}>
            <DataGrid
                rows={users}
                columns={columns}
                pageSize={5}
                rowsPerPageOptions={[5]}
                checkboxSelection
            />
        </div>
    )
}

export default AdminPage