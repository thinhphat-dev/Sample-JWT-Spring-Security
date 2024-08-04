import React, { useState } from 'react';
import classNames from 'classnames';

const LoginForm = (props) => {
    const [active, setActive] = useState("login");
    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [username, setUsername] = useState("");
    const [loginType, setLoginType] = useState("user");
    const [password, setPassword] = useState("");
    const [errors, setErrors] = useState({});

    const onChangeHandler = (event) => {
        const { name, value } = event.target;
        switch (name) {
            case "firstName":
                setFirstName(value);
                break;
            case "lastName":
                setLastName(value);
                break;
            case "username":
                setUsername(value);
                break;
            case "loginType":
                setLoginType(value);
                break;
            case "password":
                setPassword(value);
                break;
            default:
                break;
        }
    };

    const onFocusHandler = (event) => {
        const { name } = event.target;
        setErrors((prevErrors) => ({ ...prevErrors, [name]: "" }));
    };

    const validateLogin = () => {
        const newErrors = {};
        if (!username) newErrors.username = "Tên đăng nhập là bắt buộc";
        if (!password) newErrors.password = "Mật khẩu là bắt buộc";
        return newErrors;
    };

    const validateRegister = () => {
        const newErrors = {};
        if (!firstName) newErrors.firstName = "Họ là bắt buộc";
        if (!lastName) newErrors.lastName = "Tên là bắt buộc";
        if (!username) newErrors.username = "Tên đăng nhập là bắt buộc";
        if (!password) newErrors.password = "Mật khẩu là bắt buộc";
        return newErrors;
    };

    const onSubmitLogin = (e) => {
        e.preventDefault();
        const newErrors = validateLogin();
        if (Object.keys(newErrors).length > 0) {
            setErrors(newErrors);
        } else {
            setErrors({});
            props.onLogin(e, username, password);
        }
    };

    const onSubmitRegister = (e) => {
        e.preventDefault();
        const newErrors = validateRegister();
        if (Object.keys(newErrors).length > 0) {
            setErrors(newErrors);
        } else {
            setErrors({});
            props.onRegister(e, firstName, lastName, username, loginType, password);
        }
    };

    return (
        <div className="row justify-content-center">
            <div className="col-4">
                <ul className="nav nav-pills nav-justified mb-3" id="ex1" role="tablist">
                    <li className="nav-item" role="presentation">
                        <button className={classNames("nav-link", active === "login" ? "active" : "")} id="tab-login"
                            onClick={() => setActive("login")}>Đăng nhập</button>
                    </li>
                    <li className="nav-item" role="presentation">
                        <button className={classNames("nav-link", active === "register" ? "active" : "")} id="tab-register"
                            onClick={() => setActive("register")}>Đăng ký</button>
                    </li>
                </ul>

                <div className="tab-content">
                    <div className={classNames("tab-pane", "fade", active === "login" ? "show active" : "")} id="pills-login">
                        <form onSubmit={onSubmitLogin}>
                            <div className="form-outline mb-4">
                                <input type="text" id="loginUsername" name="username" className="form-control" onChange={onChangeHandler} onFocus={onFocusHandler} />
                                <label className="form-label" htmlFor="loginUsername">Tên đăng nhập</label>
                                {errors.username && <div className="text-danger">{errors.username}</div>}
                            </div>
                            <div className="form-outline mb-4">
                                <input type="password" id="loginPassword" name="password" className="form-control" onChange={onChangeHandler} onFocus={onFocusHandler} />
                                <label className="form-label" htmlFor="loginPassword">Mật khẩu</label>
                                {errors.password && <div className="text-danger">{errors.password}</div>}
                            </div>
                            <button type="submit" className="btn btn-primary btn-block mb-4">Truy cập</button>
                        </form>
                    </div>
                    <div className={classNames("tab-pane", "fade", active === "register" ? "show active" : "")} id="pills-register">
                        <form onSubmit={onSubmitRegister}>
                            <div className="form-outline mb-4">
                                <input type="text" id="registerFirstName" name="firstName" className="form-control" onChange={onChangeHandler} onFocus={onFocusHandler} />
                                <label className="form-label" htmlFor="registerFirstName">Họ</label>
                                {errors.firstName && <div className="text-danger">{errors.firstName}</div>}
                            </div>
                            <div className="form-outline mb-4">
                                <input type="text" id="registerLastName" name="lastName" className="form-control" onChange={onChangeHandler} onFocus={onFocusHandler} />
                                <label className="form-label" htmlFor="registerLastName">Tên</label>
                                {errors.lastName && <div className="text-danger">{errors.lastName}</div>}
                            </div>
                            <div className="form-outline mb-4">
                                <input type="text" id="registerUsername" name="username" className="form-control" onChange={onChangeHandler} onFocus={onFocusHandler} />
                                <label className="form-label" htmlFor="registerUsername">Tên đăng nhập</label>
                                {errors.username && <div className="text-danger">{errors.username}</div>}
                            </div>
                            <div className="form-outline mb-4">
                                <label className="form-label">Loại tài khoản</label><br />
                                <input type="radio" id="admin" name="loginType" value="admin" checked={loginType === 'admin'} onChange={onChangeHandler} />
                                <label htmlFor="admin">Admin</label><br />
                                <input type="radio" id="user" name="loginType" value="user" checked={loginType === 'user'} onChange={onChangeHandler} />
                                <label htmlFor="user">User</label>
                            </div>
                            <div className="form-outline mb-4">
                                <input type="password" id="registerPassword" name="password" className="form-control" onChange={onChangeHandler} onFocus={onFocusHandler} />
                                <label className="form-label" htmlFor="registerPassword">Mật khẩu</label>
                                {errors.password && <div className="text-danger">{errors.password}</div>}
                            </div>
                            <button type="submit" className="btn btn-primary btn-block mb-3">Đăng ký</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default LoginForm;
