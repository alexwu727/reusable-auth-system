import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react"

export const apiService = createApi({
    baseQuery: fetchBaseQuery({
        baseUrl: 'http://localhost:8080/api/v1/',
        prepareHeaders: (headers) => {
            var token = localStorage.getItem('token');
            if (!token) {
                token = sessionStorage.getItem('token');
            }
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
        verify: builder.mutation({
            query: (code) => {
                var email = localStorage.getItem('email');
                return {
                    url: 'auth/verify',
                    method: 'POST',
                    body: {
                        email: email,
                        verificationCode: code
                    },
                    responseHandler: (response) => {
                        if (response.headers.get('Content-Type').includes('text/plain')) {
                            return response.text();
                        } else {
                            return response.json();
                        }
                    }
                }
            }
        }),
        resendVerificationCode: builder.mutation({
            query: () => {
                var email = localStorage.getItem('email');
                return {
                    url: `auth/resend-verification-code?email=${email}`,
                    method: 'POST',
                    responseHandler: (response) => {
                        if (response.headers.get('Content-Type').includes('text/plain')) {
                            return response.text();
                        } else {
                            return response.json();
                        }
                    }
                }
            },
            invalidatesTags: ['User']
        }),
        login: builder.mutation({
            query: (user) => ({
                url: 'auth/login',
                method: 'POST',
                body: user
            }),
        }),
        refreshToken: builder.mutation({
            query: () => 'auth/refresh-token',
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

export const { useRegisterMutation, useVerifyMutation, useResendVerificationCodeMutation, useLoginMutation, useRefreshTokenMutation, useGetUsersQuery, useGetUserQuery, useGetCurrentUserQuery, useLazyGetCurrentUserQuery, usePatchUserMutation } = apiService;