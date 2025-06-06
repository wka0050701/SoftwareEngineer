import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

const RegisterPage = () => {
    // 如果在实际项目中使用 React Router，请取消下面的注释
    const navigate = useNavigate();

    // 使用 state 管理表单数据
    const [form, setForm] = useState({
        phone: "",
        password: "",
        nickname: "",
        default_address: ""
    });
    
    // 使用 state 管理错误信息
    const [error, setError] = useState("");
    // 使用 state 管理加载状态
    const [isLoading, setIsLoading] = useState(false);
    // 使用 state 管理注册成功状态
    const [isSuccess, setIsSuccess] = useState(false);

    // 输入框内容变化时的处理函数
    const handleChange = (e) => {
        const { name, value } = e.target;
        setForm(prevForm => ({
            ...prevForm,
            [name]: value
        }));
    };

    // 处理注册按钮点击事件
    const handleRegister = async (e) => {
        e.preventDefault();
        
        // --- 客户端输入验证 ---
        if (!/^\d{11}$/.test(form.phone)) {
            setError("手机号必须是11位数字");
            return;
        }
        if (form.password.length < 8) {
            setError("密码长度不能少于8位");
            return;
        }
        if (!form.nickname.trim()) {
            setError("昵称不能为空");
            return;
        }
        if (!form.default_address.trim()) {
            setError("收货地址不能为空");
            return;
        }

        setError("");
        setIsLoading(true);

        try {
            const response = await fetch("/api/auth/register", {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(form)
            });
            
            const result = await response.json();
            
            if (result.code === 1) {
                setIsSuccess(true);
                console.log("注册成功，用户ID:", result.data.user_id);
                // 注册成功后，3秒后跳转到登录页
                setTimeout(() => {
                    navigate("/login");
                }, 3000);
            } else {
                setError(result.message || "注册失败，请稍后重试");
            }
        } catch (err) {
            console.error("注册失败", err);
            setError("系统错误，请稍后再试");
        } finally {
            setIsLoading(false);
        }
    };

    // 注册成功后显示的界面
    if (isSuccess) {
        return (
            <div className="flex items-center justify-center min-h-screen bg-gray-100">
                <div className="w-full max-w-md p-8 space-y-6 bg-white rounded-lg shadow-lg text-center">
                     <svg className="mx-auto h-12 w-12 text-green-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
                    </svg>
                    <h2 className="text-2xl font-bold text-gray-800">注册成功！</h2>
                    <p className="text-gray-600">欢迎你，{form.nickname}！即将跳转至登录页面...</p>
                </div>
            </div>
        );
    }

    // 渲染注册表单
    return (
        <div className="flex items-center justify-center min-h-screen bg-gray-100 px-4">
            <div className="w-full max-w-md p-8 space-y-6 bg-white rounded-xl shadow-lg">
                <h2 className="text-3xl font-bold text-center text-gray-900">创建您的账户</h2>
                
                <form onSubmit={handleRegister} className="space-y-6">
                    {/* 输入字段 */}
                    <div>
                        <label htmlFor="phone" className="text-sm font-medium text-gray-700">手机号</label>
                        <input 
                            id="phone" name="phone" type="tel" required 
                            className="mt-1 block w-full px-3 py-2 bg-gray-50 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                            placeholder="请输入11位手机号"
                            value={form.phone} 
                            onChange={handleChange} 
                        />
                    </div>
                    
                    <div>
                        <label htmlFor="password" class="text-sm font-medium text-gray-700">密码</label>
                        <input 
                            id="password" name="password" type="password" required 
                            className="mt-1 block w-full px-3 py-2 bg-gray-50 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                            placeholder="至少8位"
                            value={form.password} 
                            onChange={handleChange} 
                        />
                    </div>

                    <div>
                        <label htmlFor="nickname" class="text-sm font-medium text-gray-700">昵称</label>
                        <input 
                            id="nickname" name="nickname" type="text" required 
                            className="mt-1 block w-full px-3 py-2 bg-gray-50 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                            placeholder="您的称呼"
                            value={form.nickname} 
                            onChange={handleChange} 
                        />
                    </div>
                     
                    <div>
                        <label htmlFor="default_address" class="text-sm font-medium text-gray-700">收货地址</label>
                        <input 
                            id="default_address" name="default_address" type="text" required 
                            className="mt-1 block w-full px-3 py-2 bg-gray-50 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                            placeholder="例如：北京市海淀区中关村大街1号"
                            value={form.default_address} 
                            onChange={handleChange} 
                        />
                    </div>
                    
                    {error && <p className="text-sm text-red-600 text-center">{error}</p>}

                    {/* 提交按钮 */}
                    <div>
                        <button 
                            type="submit" 
                            disabled={isLoading}
                            className="w-full flex justify-center py-3 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 disabled:bg-indigo-300 disabled:cursor-not-allowed"
                        >
                            {isLoading ? <div className="w-6 h-6 border-4 border-white border-t-transparent rounded-full animate-spin"></div> : '立即注册'}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
};

export default RegisterPage;
