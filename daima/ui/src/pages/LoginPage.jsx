import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

const LoginPage = () => {
  const [phone, setPhone] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleLogin = async () => {
    if (!/^\d{11}$/.test(phone)) {
      setError("请输入11位手机号");
      return;
    }

    try {
      const res = await fetch("/api/auth/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ phone, password }),
      });
      const result = await res.json();

      if (result.code === 1) {
        const userId = result.data.user_id;
        // 存到 localStorage 或全局状态
        localStorage.setItem("userId", userId);
        // 商家是 user_id 为 1
        navigate(userId === 1 ? "/admin" : "/user");
      } else {
        setError(result.message);
      }
    } catch (err) {
      console.error("登录失败", err);
      setError("系统错误");
    }
  };

  return (
    <div>
      <h2>登录</h2>
      <input value={phone} onChange={e => setPhone(e.target.value)} placeholder="手机号" />
      <input value={password} onChange={e => setPassword(e.target.value)} type="password" placeholder="密码" />
      <button onClick={handleLogin}>登录</button>
      {error && <p style={{ color: "red" }}>{error}</p>}
    </div>
  );
};

export default LoginPage;
