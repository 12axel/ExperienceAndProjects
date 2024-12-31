'use client';

import React, { useState } from 'react';
import axios from 'axios';

const baseURL = "https://ziek69aur9.execute-api.us-east-2.amazonaws.com/initial";

export default function AccountPage() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [userType, setUserType] = useState<'Seller' | 'Buyer'>('Seller');
    const [message, setMessage] = useState<string | null>(null);
    const [error, setError] = useState<string | null>(null);

    const handleUsernameChange = (e: React.ChangeEvent<HTMLInputElement>) => setUsername(e.target.value);
    const handlePasswordChange = (e: React.ChangeEvent<HTMLInputElement>) => setPassword(e.target.value);
    const handleUserTypeChange = (e: React.ChangeEvent<HTMLSelectElement>) => setUserType(e.target.value as 'Seller' | 'Buyer');

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setMessage(null);
        setError(null);

        try {
            const response = await axios.post(`${baseURL}/create-account`, {
                body: {
                    username,
                    password,
                    userType,
                },
            });

            if (response.data.statusCode === 400) {
                setError(response.data.body.message || 'Account creation failed: Username already exists.');
            } else if (response.status === 200) {
                setMessage('Account created successfully!');
                setUsername('');
                setPassword('');
                setUserType('Seller'); // Reset to default
            }
        } catch (err: any) {
            if (err.response && err.response.status === 400) {
                setError(err.response.data.message || 'Account creation failed.');
            } else {
                setError('Failed to create account. Please try again.');
            }
        }
    };

    return (
        <main className="container mx-auto mt-5">
            <h1 className="text-4xl mb-6">Create Account</h1>

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

                <div>
                    <label className="block text-lg mb-2">User Type</label>
                    <select
                        value={userType}
                        onChange={handleUserTypeChange}
                        className="w-full p-2 border border-gray-300 rounded"
                    >
                        <option value="Seller">Seller</option>
                        <option value="Buyer">Buyer</option>
                    </select>
                </div>

                <button
                    type="submit"
                    className="w-full p-2 mt-4 bg-blue-500 text-white rounded hover:bg-blue-600"
                >
                    Create Account
                </button>
            </form>
        </main>
    );
}
