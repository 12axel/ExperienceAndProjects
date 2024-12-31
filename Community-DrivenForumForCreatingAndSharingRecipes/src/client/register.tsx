// Login.tsx
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';


interface RegisterProps {
    onRegister: (userData: UserData) => void;
}

interface UserData {
	email: string;
}

interface RegisterResponse {
	success: boolean;
	message: string;
	user?: UserData;
}

const Register: React.FC<RegisterProps> = ({onRegister}) => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [loading, setIsLoading] = useState(false);
    const navigate = useNavigate();
    const handleRegister = async (e: React.FormEvent) => {
        e.preventDefault();
	setError('');
	setIsLoading(true);
	
	try {
		const response = await fetch('/newuser', {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json',
			},
			body: JSON.stringify({
				username,
				password,
			}),
		});

		const data: RegisterResponse = await response.json();
		console.log(data);
		if (data.success && data.user) {
			navigate('/');
		}
		else {
			setError(data.message || 'Invalid username or password');	
		}	
	} catch (error) {
		setError('An error occured while registering');
		console.error('Register error: ', error);
	} finally {
		setIsLoading(false);
	}
    };


    return (
        <div className="row pt-5 justify-content-center">
            <div className="col max-width">
                <div className="card">
                    <div className="card-body">
                        <h2 className="card-title text-center mb-4">Register</h2>
                        <form onSubmit={handleRegister}>
                            <div className="mb-3">
                                <label htmlFor="username" className="form-label">Email</label>
                                <input
                                    type="text"
                                    className="form-control"
                                    id="username"
                                    value={username}
                                    onChange={(e) => setUsername(e.target.value)}
                                    required
                                />
                            </div>
                            <div className="mb-3">
                                <label htmlFor="password" className="form-label">Password</label>
                                <input
                                    type="password"
                                    className="form-control"
                                    id="password"
                                    value={password}
                                    onChange={(e) => setPassword(e.target.value)}
                                    required
                                />
                            </div>
                            {error && <p className="text-danger">{error}</p>}
                            <button type="submit" className="btn btn-primary w-100">Register</button>
                        </form>
                    </div>
                    <div className="card-footer">
                        <a href="/" className='card-link'>Login Here</a>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Register;
