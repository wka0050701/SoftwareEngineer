import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

const RegisterPage = () => {
  const [form, setForm] = useState({
    phone: "",
    password: "",
    nickname: "",
    default_address: ""
  });
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleRegister = async () => {
    if (!/^\d{11}$/.test(form.phone)) {
      setError("手机号必须是11位数字");
      return;
    }

    try {
      const res = await fetch("/api/auth/register", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(form),
      });
      const result = await res.json();
      if (result.code === 1) {
        navigate("/login"); // 注册成功跳转登录
      } else {
        setError(result.message);
      }
    } catch (err) {
      console.error("注册失败", err);
      setError("系统错误");
    }
  };

  return (
    <div>
      <h2>注册</h2>
      <input placeholder="手机号" value={form.phone} onChange={e => setForm({ ...form, phone: e.target.value })} />
      <input placeholder="密码" type="password" value={form.password} onChange={e => setForm({ ...form, password: e.target.value })} />
      <input placeholder="昵称" value={form.nickname} onChange={e => setForm({ ...form, nickname: e.target.value })} />
      <input placeholder="地址" value={form.default_address} onChange={e => setForm({ ...form, default_address: e.target.value })} />
      <button onClick={handleRegister}>注册</button>
      {error && <p style={{ color: "red" }}>{error}</p>}
    </div>
  );
};

export default RegisterPage;
