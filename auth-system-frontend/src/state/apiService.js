import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react"

export const apiService = createApi({
    baseQuery: fetchBaseQuery({
        baseUrl: 'http://localhost:8080/api/v1/',
        prepareHeaders: (headers, { getState }) => {
            const token = localStorage.getItem('token');
            if (token) {
                headers.set('Authorization', `Bearer ${token}`);
            }
            return headers;
        }
    }),
    reducerPath: 'api',
    tagTypes: ['User'],
    endpoints: (builder) => ({
        register: builder.mutation({
            query: (user) => ({
                url: 'auth/register',
                method: 'POST',
                body: user
            }),
            invalidatesTags: ['User']
        }),
        verify: builder.query({
            query: (code) => {
                var email = localStorage.getItem('email');
                console.log("triggered verify query");
                console.log(`auth/verify?email=${email}&verificationCode=${code}`)
                return {
                    url: `auth/verify?email=${email}&verificationCode=${code}`,
                    responseHandler: (response) => {
                        if (response.headers.get('Content-Type').includes('text/plain')) {
                            return response.text();
                        } else {
                            return response.json();
                        }
                    }
                }
            },
            providesTags: ['User']
        }),
        login: builder.mutation({
            query: (user) => ({
                url: 'auth/login',
                method: 'POST',
                body: user
            }),
        }),
        getUsers: builder.query({
            query: () => 'users',
            providesTags: ['User']
        }),
        getUser: builder.query({
            query: (id) => `users/${id}`,
            providesTags: ['User']
        }),
        getCurrentUser: builder.query({
            query: () => 'users/info',
            providesTags: ['User']
        }),
        patchUser: builder.mutation({
            query: ({ id, user }) => ({
                url: `users/${id}`,
                method: 'PATCH',
                header: {
                    'Content-Type': 'application/json-patch+json'
                },
                body: user
            }),
            invalidatesTags: ['User']
        }),
    })
});

export const { useRegisterMutation, useLazyVerifyQuery, useLoginMutation, useGetUsersQuery, useGetUserQuery, useGetCurrentUserQuery, usePatchUserMutation } = apiService;