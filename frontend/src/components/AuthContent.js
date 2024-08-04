import React, { useState, useEffect } from 'react';
import Teacher from './Teacher'; 
import { request, setAuthHeader } from '../helpers/axios_helper';

const AuthContent = ({ token }) => {
    const [data, setData] = useState([]);

    useEffect(() => {
        if (token) {
            setAuthHeader(token); // Thiết lập header với token
            request(
                "GET",
                "/messages",
                {}).then(
                (response) => {
                    // console.log("Response data: ", response.data);
                    setData(response.data);
                }).catch(
                (error) => {
                    if (error.response && error.response.status === 401) {
                        setAuthHeader(null);
                    } else {
                        setData(error.response ? error.response.code : "Unknown error");
                    }
                }
            );
        }
    }, [token]); // Chạy lại effect khi token thay đổi

    return (
        <div className="container mt-5">
            <div className="row justify-content-center">
                {token ? (
                    <Teacher />
                ) : (
                    <h1>Xin mời đăng nhập</h1>
                )}
            </div>
        </div>
    );
};

export default AuthContent;
