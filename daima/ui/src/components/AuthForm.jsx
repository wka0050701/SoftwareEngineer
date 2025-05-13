import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

const AuthForm = ({ type }) => {
  const isLogin = type === "login";
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    phone: "",
    password: "",
    nickname: "",
    default_address: "",
  });

  const [error, setError] = useState("");

  const handleChange = (e) => {
    setFormData((prev) => ({ ...prev, [e.target.name]: e.target.value }));
  };

  const validatePhone = (phone) => /^\\d{11}$/.test(phone);

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!validatePhone(formData.phone)) {
      setError("手机号必须为11位数字");
      return;
    }

    try {
      const response = await fetch(`/api/auth/${isLogin ? "login" : "register"}`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(formData),
      });

      const result = await response.json();

      if (result.code === 1) {
        const userId = result.data.user_id;
        navigate(userId === 1 ? "/admin" : "/home");
      } else {
        setError(result.message || "操作失败");
      }
    } catch (err) {
      setError("网络错误，请稍后重试");
    }
  };

  return (
    <div style={{ maxWidth: 400, margin: "50px auto" }}>
      <h2>{isLogin ? "登录" : "注册"}</h2>
      <form onSubmit={handleSubmit}>
        <div>
          <label>手机号：</label>
          <input
            type="text"
            name="phone"
            value={formData.phone}
            onChange={handleChange}
            required
          />
        </div>
        <div>
          <label>密码：</label>
          <input
            type="password"
            name="password"
            value={formData.password}
            onChange={handleChange}
            required
          />
        </div>
        {!isLogin && (
          <>
            <div>
              <label>昵称：</label>
              <input
                type="text"
                name="nickname"
                value={formData.nickname}
                onChange={handleChange}
                required
              />
            </div>
            <div>
              <label>默认地址：</label>
              <input
                type="text"
                name="default_address"
                value={formData.default_address}
                onChange={handleChange}
                required
              />
            </div>
          </>
        )}
        {error && <p style={{ color: "red" }}>{error}</p>}
        <button type="submit">{isLogin ? "登录" : "注册"}</button>
      </form>
    </div>
  );
};

export default AuthForm;
