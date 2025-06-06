import React, { useState } from "react";
import { useNavigate, Link } from "react-router-dom";

const LoginPage = () => {
  const [form, setForm] = useState({
    phone: "",
    password: ""
  });
  const [error, setError] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm(prevForm => ({
      ...prevForm,
      [name]: value
    }));
  };

  const handleLogin = async (e) => {
    e.preventDefault();
    
    if (!/^\d{11}$/.test(form.phone)) {
      setError("请输入11位手机号");
      return;
    }
    if (form.password.length < 8) {
      setError("密码长度不能少于8位");
      return;
    }

    setError("");
    setIsLoading(true);

    try {
      const response = await fetch("/api/auth/login", {
        method: "POST",
        headers: { 
          "Content-Type": "application/json"
        },
        body: JSON.stringify(form)
      });
      
      const result = await response.json();

      if (result.code === 1) {
        const userId = result.data.user_id;
        // 存储用户ID到localStorage
        localStorage.setItem("userId", userId);
        // 根据用户ID跳转到不同页面
        navigate(userId === 1 ? "/admin" : "/user-dashboard");
      } else {
        setError(result.message || "登录失败，请检查账号密码");
      }
    } catch (err) {
      console.error("登录失败", err);
      setError("系统错误，请稍后再试");
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="flex items-center justify-center min-h-screen bg-gray-100 px-4">
      <div className="w-full max-w-md p-8 space-y-6 bg-white rounded-xl shadow-lg">
        <h2 className="text-3xl font-bold text-center text-gray-900">登录账户</h2>
        
        <form onSubmit={handleLogin} className="space-y-6">
          <div>
            <label htmlFor="phone" className="text-sm font-medium text-gray-700">手机号</label>
            <input 
              id="phone" 
              name="phone" 
              type="tel" 
              required 
              className="mt-1 block w-full px-3 py-2 bg-gray-50 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
              placeholder="请输入11位手机号"
              value={form.phone} 
              onChange={handleChange} 
            />
          </div>
          
          <div>
            <label htmlFor="password" className="text-sm font-medium text-gray-700">密码</label>
            <input 
              id="password" 
              name="password" 
              type="password" 
              required 
              className="mt-1 block w-full px-3 py-2 bg-gray-50 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
              placeholder="请输入密码"
              value={form.password} 
              onChange={handleChange} 
            />
          </div>

          {error && <p className="text-sm text-red-600 text-center">{error}</p>}

          <div>
            <button 
              type="submit" 
              disabled={isLoading}
              className="w-full flex justify-center py-3 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 disabled:bg-indigo-300 disabled:cursor-not-allowed"
            >
              {isLoading ? <div className="w-6 h-6 border-4 border-white border-t-transparent rounded-full animate-spin"></div> : '登录'}
            </button>
          </div>

          <div className="text-center">
            <Link to="/register" className="text-sm text-indigo-600 hover:text-indigo-500">
              还没有账号？立即注册
            </Link>
            <div className="mt-2">
              <Link to="/forgot-password" className="text-sm text-indigo-600 hover:text-indigo-500">
                忘记密码？
              </Link>
            </div>
          </div>
        </form>
      </div>
    </div>
  );
};

export default LoginPage;
