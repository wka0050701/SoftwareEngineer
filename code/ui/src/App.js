import React from "react";
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import LoginPage from "./pages/LoginPage";
import RegisterPage from "./pages/RegisterPage";
import UserDashboardPage from "./pages/UserDashboardPage";
import ForgotPasswordPage from "./pages/ForgotPasswordPage";
import UserInfoPage from "./pages/UserInfoPage";
import ProductManagementPage from "./pages/admin/ProductManagementPage";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/forgot-password" element={<ForgotPasswordPage />} />
        <Route path="/user-info" element={<UserInfoPage />} />
        <Route path="/admin/products" element={<ProductManagementPage />} />
        <Route path="*" element={<Navigate to="/login" replace />} />
        <Route path="/user-dashboard" element={<UserDashboardPage />} />
      </Routes>
    </Router>
  );
}

export default App
