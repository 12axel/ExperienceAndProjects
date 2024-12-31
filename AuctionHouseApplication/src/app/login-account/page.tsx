'use client';

import React, { useState } from 'react';
import axios from 'axios';
import { useRouter } from 'next/navigation';

const baseURL = "https://ziek69aur9.execute-api.us-east-2.amazonaws.com/initial";

export default function LoginPage() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [message, setMessage] = useState<string | null>(null);
    const [error, setError] = useState<string | null>(null);

    const handleUsernameChange = (e: React.ChangeEvent<HTMLInputElement>) => setUsername(e.target.value);
    const handlePasswordChange = (e: React.ChangeEvent<HTMLInputElement>) => setPassword(e.target.value);

    const router = useRouter();

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setMessage(null);
        setError(null);

        try {
            const response = await axios.post(`${baseURL}/login-account`, {
                body: {
                    username,
                    password,
                },
            }, {
                headers: {
                    'Content-Type': 'application/json',
                }
            });

            const { statusCode, body } = response.data;
            if (statusCode === 200) {
                const parsedBody = JSON.parse(body);
                const { userType, message } = parsedBody;

                setMessage(message || 'Login successful!');

                // Store user details in sessionStorage
                sessionStorage.setItem('userName', username);
                sessionStorage.setItem('userType', userType); 
                sessionStorage.setItem('password', password);

                // Emit a custom event to notify other components
                window.dispatchEvent(new Event('sessionUpdated'));

                // Redirect based on userType
                // if (userType === "Admin") {
                //     router.push("/admin-dashboard");
                // } else {
                router.push("/");
                // }
            } else if (statusCode === 401) {
                setError('Invalid username or password.');
            } else if (statusCode === 403) {
                setError('Account is closed and cannot be logged in.');
            } else {
                setError(message || 'An unexpected error occurred. Please try again later.');
            }
        } catch (err) {
            console.error("Error caught in catch block:", err);
            setError('An unexpected error occurred. Please try again later.');
        }
    };

    return (
        <main className="container mx-auto mt-5">
            <h1 className="text-4xl mb-6">Login</h1>

            {/* Display success or error messages */}
            {message && <div className="text-green-500 mb-4">{message}</div>}
            {error && <div className="text-red-500 mb-4">{error}</div>}

            <form onSubmit={handleSubmit} className="space-y-4">
                <div>
                    <label className="block text-lg mb-2">Username</label>
                    <input
                        type="text"
                        value={username}
                        onChange={handleUsernameChange}
                        required
                        className="w-full p-2 border border-gray-300 rounded"
                    />
                </div>

                <div>
                    <label className="block text-lg mb-2">Password</label>
                    <input
                        type="password"
                        value={password}
                        onChange={handlePasswordChange}
                        required
                        className="w-full p-2 border border-gray-300 rounded"
                    />
                </div>

                <button
                    type="submit"
                    className="w-full p-2 mt-4 bg-blue-500 text-white rounded hover:bg-blue-600"
                >
                    Login
                </button>
            </form>
        </main>
    );
}
