import React, { useState, useEffect } from "react";

const UserDashboardPage = () => {
  const userId = localStorage.getItem("user_id");
  const [userData, setUserData] = useState(null);
  const [showEditForm, setShowEditForm] = useState(false);
  const [formData, setFormData] = useState({
    phone: "",
    password: "",
    nickname: "",
    default_address: "",
  });
  const [message, setMessage] = useState("");

  useEffect(() => {
    if (!userId) return;
    fetch(`/api/user/${userId}`)
      .then((res) => res.json())
      .then((res) => {
        if (res.code === 1) {
          setUserData(res.data);
          setFormData(res.data);
        }
      });
  }, [userId]);

  const handleChange = (e) => {
    setFormData((prev) => ({ ...prev, [e.target.name]: e.target.value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const res = await fetch(`/api/user/${userId}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(formData),
      });
      const result = await res.json();
      if (result.code === 1) {
        setMessage("修改成功");
        setUserData(formData);
        setShowEditForm(false);
      } else {
        setMessage("修改失败: " + result.message);
      }
    } catch (err) {
      setMessage("网络错误");
    }
  };

  if (!userData) return <div>加载中...</div>;

  return (
    <div style={{ maxWidth: 500, margin: "50px auto" }}>
      <h2>欢迎，{userData.nickname}</h2>
      <p>手机号：{userData.phone}</p>
      <p>默认地址：{userData.default_address}</p>
      <button onClick={() => setShowEditForm(!showEditForm)}>
        {showEditForm ? "取消修改" : "修改信息"}
      </button>
      {showEditForm && (
        <form onSubmit={handleSubmit} style={{ marginTop: 20 }}>
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
          <button type="submit">保存</button>
        </form>
      )}
      {message && <p style={{ color: "green" }}>{message}</p>}
    </div>
  );
};

export default UserDashboardPage;
