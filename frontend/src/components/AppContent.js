import React, { useState } from 'react';
import { getAuthToken, request, setAuthHeader } from '../helpers/axios_helper';
import Buttons from './Buttons';
import AuthContent from './AuthContent';
import LoginForm from './LoginForm';
import WelcomeContent from './WelcomeContent';

const AppContent = () => {
    const [componentToShow, setComponentToShow] = useState("welcome");
    const [token, setToken] = useState(getAuthToken()); // Lấy token từ localStorage

    const login = () => {
        setComponentToShow("login");
    };

    const logout = () => {
        setComponentToShow("welcome");
        setToken(null);
        setAuthHeader(null);
    };

    const onLogin = (e, username, password) => {
        e.preventDefault();
        request(
            "POST",
            "/login",
            {
                username: username,
                password: password
            }).then(
            (response) => {
                // console.log('Login response:', response.data); // Log response data
                setAuthHeader(response.data.token);
                setToken(response.data.token);
                setComponentToShow("messages");
            }).catch(
            (error) => {
                console.error('Login error:', error); // Log error
                setAuthHeader(null);
                setToken(null);
                setComponentToShow("welcome");
            }
        );
    };

    const onRegister = (event, firstName, lastName, username, loginType, password) => {
        event.preventDefault();
        const payload = {
            firstName: firstName,
            lastName: lastName,
            username: username,
            role: loginType,
            password: password
        };
        // console.log('Registration payload:', payload); // Log payload
        request(
            "POST",
            "/register",
            payload
        ).then(
            (response) => {
                // console.log('Register response:', response.data); // Log response data
                setAuthHeader(response.data.token);
                setToken(response.data.token);
                setComponentToShow("messages");
            }).catch(
            (error) => {
                console.error('Register error:', error); // Log error
                setAuthHeader(null);
                setToken(null);
                setComponentToShow("welcome");
            }
        );
    };

    return (
        <>
            <Buttons
                login={login}
                logout={logout}
            />

            {componentToShow === "welcome" && <WelcomeContent />}
            {componentToShow === "login" && <LoginForm onLogin={onLogin} onRegister={onRegister} />}
            {componentToShow === "messages" && <AuthContent token={token} />}
        </>
    );
};

export default AppContent;
