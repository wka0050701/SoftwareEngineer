import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";

const UserInfoPage = () => {
    const [userInfo, setUserInfo] = useState({
        phone: "",
        password: "",
        nickname: "",
        default_address: ""
    });
    const [isEditing, setIsEditing] = useState(false);
    const [error, setError] = useState("");
    const [isLoading, setIsLoading] = useState(false);
    const [isSuccess, setIsSuccess] = useState(false);
    const navigate = useNavigate();

    // 获取用户信息
    const fetchUserInfo = async () => {
        setIsLoading(true);
        try {
            const response = await fetch("/api/user/query", {
                method: "GET",
                headers: {
                    "Content-Type": "application/json"
                }
            });
            
            const result = await response.json();

            if (result.code === 1) {
                setUserInfo(result.data);
            } else {
                setError(result.message || "获取用户信息失败");
            }
        } catch (err) {
            console.error("获取用户信息失败", err);
            setError("系统错误，请稍后再试");
        } finally {
            setIsLoading(false);
        }
    };

    // 组件加载时获取用户信息
    useEffect(() => {
        fetchUserInfo();
    }, []);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setUserInfo(prev => ({
            ...prev,
            [name]: value
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        
        // 表单验证
        if (!/^\d{11}$/.test(userInfo.phone)) {
            setError("手机号必须是11位数字");
            return;
        }
        if (userInfo.password.length < 8) {
            setError("密码长度不能少于8位");
            return;
        }
        if (!userInfo.nickname.trim()) {
            setError("昵称不能为空");
            return;
        }
        if (!userInfo.default_address.trim()) {
            setError("收货地址不能为空");
            return;
        }

        setError("");
        setIsLoading(true);

        try {
            const response = await fetch("/api/user/modify", {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(userInfo)
            });
            
            const result = await response.json();

            if (result.code === 1) {
                setIsSuccess(true);
                setIsEditing(false);
                // 3秒后清除成功提示
                setTimeout(() => {
                    setIsSuccess(false);
                }, 3000);
            } else {
                setError(result.message || "修改失败，请稍后重试");
            }
        } catch (err) {
            console.error("修改失败", err);
            setError("系统错误，请稍后再试");
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className="min-h-screen bg-gray-100 py-12 px-4 sm:px-6 lg:px-8">
            <div className="max-w-md mx-auto bg-white rounded-xl shadow-lg p-8">
                <div className="flex justify-between items-center mb-6">
                    <h2 className="text-2xl font-bold text-gray-900">个人信息</h2>
                    <button
                        onClick={() => navigate("/user-dashboard")}
                        className="text-sm text-indigo-600 hover:text-indigo-500"
                    >
                        返回首页
                    </button>
                </div>

                {isSuccess && (
                    <div className="mb-4 p-4 bg-green-50 text-green-700 rounded-md">
                        修改成功！
                    </div>
                )}

                {error && (
                    <div className="mb-4 p-4 bg-red-50 text-red-700 rounded-md">
                        {error}
                    </div>
                )}

                <form onSubmit={handleSubmit} className="space-y-6">
                    <div>
                        <label htmlFor="phone" className="block text-sm font-medium text-gray-700">手机号</label>
                        <input
                            id="phone"
                            name="phone"
                            type="tel"
                            required
                            disabled={!isEditing}
                            className="mt-1 block w-full px-3 py-2 bg-gray-50 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 disabled:bg-gray-100"
                            value={userInfo.phone}
                            onChange={handleChange}
                        />
                    </div>

                    <div>
                        <label htmlFor="password" className="block text-sm font-medium text-gray-700">密码</label>
                        <input
                            id="password"
                            name="password"
                            type="password"
                            required
                            disabled={!isEditing}
                            className="mt-1 block w-full px-3 py-2 bg-gray-50 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 disabled:bg-gray-100"
                            value={userInfo.password}
                            onChange={handleChange}
                        />
                    </div>

                    <div>
                        <label htmlFor="nickname" className="block text-sm font-medium text-gray-700">昵称</label>
                        <input
                            id="nickname"
                            name="nickname"
                            type="text"
                            required
                            disabled={!isEditing}
                            className="mt-1 block w-full px-3 py-2 bg-gray-50 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 disabled:bg-gray-100"
                            value={userInfo.nickname}
                            onChange={handleChange}
                        />
                    </div>

                    <div>
                        <label htmlFor="default_address" className="block text-sm font-medium text-gray-700">收货地址</label>
                        <input
                            id="default_address"
                            name="default_address"
                            type="text"
                            required
                            disabled={!isEditing}
                            className="mt-1 block w-full px-3 py-2 bg-gray-50 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 disabled:bg-gray-100"
                            value={userInfo.default_address}
                            onChange={handleChange}
                        />
                    </div>

                    <div className="flex justify-end space-x-4">
                        {isEditing ? (
                            <>
                                <button
                                    type="button"
                                    onClick={() => {
                                        setIsEditing(false);
                                        fetchUserInfo(); // 重新获取用户信息，放弃修改
                                    }}
                                    className="px-4 py-2 border border-gray-300 rounded-md text-sm font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
                                >
                                    取消
                                </button>
                                <button
                                    type="submit"
                                    disabled={isLoading}
                                    className="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 disabled:bg-indigo-300"
                                >
                                    {isLoading ? "保存中..." : "保存"}
                                </button>
                            </>
                        ) : (
                            <button
                                type="button"
                                onClick={() => setIsEditing(true)}
                                className="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
                            >
                                编辑信息
                            </button>
                        )}
                    </div>
                </form>
            </div>
        </div>
    );
};

export default UserInfoPage; 