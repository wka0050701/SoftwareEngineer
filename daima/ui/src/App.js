import React from "react";
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import LoginPage from "./pages/LoginPage";
import RegisterPage from "./pages/RegisterPage";
import UserDashboardPage from "./pages/UserDashboardPage";


function App() {
  return (
    <Router>
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="*" element={<Navigate to="/login" replace />} />
        <Route path="/user-dashboard" element={<UserDashboardPage />} />
      </Routes>
    </Router>
  );
}

export default App;




// //项目根组件

// //app -> index.js ->public/index.htm(root)
// const list = [
//   {id:'1',name:'a'},
//   {id:'2',name:'b'},
//   {id:'3',name:'c'}
// ]
// function App() {
//   return (
//     <div className="App">
//       <ul>
//         {/* {渲染列表需要key}  */}
//         {list.map(item=><li key={item.id}>{item.name}</li>)}
//       </ul>
//     </div>
//   );
// }

// export default App;
