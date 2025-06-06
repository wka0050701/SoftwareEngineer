import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";

const ProductManagementPage = () => {
    const [products, setProducts] = useState([]);
    const [total, setTotal] = useState(0);
    const [currentPage, setCurrentPage] = useState(1);
    const [pageSize, setPageSize] = useState(10);
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState("");
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [editingProduct, setEditingProduct] = useState(null);
    const [formData, setFormData] = useState({
        category_id: "",
        name: "",
        price: "",
        description: "",
        stock: "",
        status: 1,
        image: ""
    });
    const navigate = useNavigate();

    // 获取菜品列表
    const fetchProducts = async () => {
        setIsLoading(true);
        try {
            const response = await fetch(`/api/admin/product/list?pageNum=${currentPage}&pageSize=${pageSize}`, {
                method: "GET",
                headers: {
                    "Content-Type": "application/json"
                }
            });
            
            const result = await response.json();

            if (result.code === 1) {
                setProducts(result.data.list);
                setTotal(result.data.total);
            } else {
                setError(result.message || "获取菜品列表失败");
            }
        } catch (err) {
            console.error("获取菜品列表失败", err);
            setError("系统错误，请稍后再试");
        } finally {
            setIsLoading(false);
        }
    };

    // 组件加载时获取菜品列表
    useEffect(() => {
        fetchProducts();
    }, [currentPage, pageSize]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        
        // 表单验证
        if (!formData.name.trim()) {
            setError("菜品名称不能为空");
            return;
        }
        if (!formData.price || formData.price <= 0) {
            setError("价格必须大于0");
            return;
        }
        if (!formData.stock || formData.stock < 0) {
            setError("库存不能为负数");
            return;
        }

        setError("");
        setIsLoading(true);

        try {
            const url = editingProduct 
                ? "/api/admin/product" 
                : "/api/admin/product";
            
            const method = editingProduct ? "PUT" : "POST";
            
            const response = await fetch(url, {
                method,
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(editingProduct 
                    ? { ...formData, productId: editingProduct.product_id }
                    : formData
                )
            });
            
            const result = await response.json();

            if (result.code === 1) {
                setIsModalOpen(false);
                setEditingProduct(null);
                setFormData({
                    category_id: "",
                    name: "",
                    price: "",
                    description: "",
                    stock: "",
                    status: 1,
                    image: ""
                });
                fetchProducts();
            } else {
                setError(result.message || "操作失败，请稍后重试");
            }
        } catch (err) {
            console.error("操作失败", err);
            setError("系统错误，请稍后再试");
        } finally {
            setIsLoading(false);
        }
    };

    const handleDelete = async (productId) => {
        if (!window.confirm("确定要删除这个菜品吗？")) {
            return;
        }

        setIsLoading(true);
        try {
            const response = await fetch("/api/admin/product", {
                method: "DELETE",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({ product_id: productId })
            });
            
            const result = await response.json();

            if (result.code === 1) {
                fetchProducts();
            } else {
                setError(result.message || "删除失败，请稍后重试");
            }
        } catch (err) {
            console.error("删除失败", err);
            setError("系统错误，请稍后再试");
        } finally {
            setIsLoading(false);
        }
    };

    const handleEdit = (product) => {
        setEditingProduct(product);
        setFormData({
            category_id: product.category_id,
            name: product.name,
            price: product.price,
            description: product.description,
            stock: product.stock,
            status: product.status,
            image: product.image || ""
        });
        setIsModalOpen(true);
    };

    return (
        <div className="min-h-screen bg-gray-100 py-12 px-4 sm:px-6 lg:px-8">
            <div className="max-w-7xl mx-auto">
                <div className="flex justify-between items-center mb-6">
                    <h2 className="text-2xl font-bold text-gray-900">菜品管理</h2>
                    <button
                        onClick={() => {
                            setEditingProduct(null);
                            setFormData({
                                category_id: "",
                                name: "",
                                price: "",
                                description: "",
                                stock: "",
                                status: 1,
                                image: ""
                            });
                            setIsModalOpen(true);
                        }}
                        className="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700"
                    >
                        添加菜品
                    </button>
                </div>

                {error && (
                    <div className="mb-4 p-4 bg-red-50 text-red-700 rounded-md">
                        {error}
                    </div>
                )}

                {/* 菜品列表 */}
                <div className="bg-white shadow overflow-hidden sm:rounded-md">
                    <ul className="divide-y divide-gray-200">
                        {products.map((product) => (
                            <li key={product.product_id} className="px-6 py-4">
                                <div className="flex items-center justify-between">
                                    <div className="flex-1 min-w-0">
                                        <h3 className="text-lg font-medium text-gray-900">{product.name}</h3>
                                        <p className="mt-1 text-sm text-gray-500">{product.description}</p>
                                        <div className="mt-2 flex items-center text-sm text-gray-500">
                                            <span className="mr-4">价格: ¥{product.price}</span>
                                            <span className="mr-4">库存: {product.stock}</span>
                                            <span>状态: {product.status === 1 ? "上架" : "下架"}</span>
                                        </div>
                                    </div>
                                    <div className="ml-4 flex-shrink-0 flex space-x-4">
                                        <button
                                            onClick={() => handleEdit(product)}
                                            className="text-indigo-600 hover:text-indigo-900"
                                        >
                                            编辑
                                        </button>
                                        <button
                                            onClick={() => handleDelete(product.product_id)}
                                            className="text-red-600 hover:text-red-900"
                                        >
                                            删除
                                        </button>
                                    </div>
                                </div>
                            </li>
                        ))}
                    </ul>
                </div>

                {/* 分页 */}
                <div className="mt-4 flex justify-between items-center">
                    <div className="flex items-center">
                        <span className="text-sm text-gray-700">
                            共 {total} 条记录
                        </span>
                    </div>
                    <div className="flex items-center space-x-2">
                        <button
                            onClick={() => setCurrentPage(prev => Math.max(prev - 1, 1))}
                            disabled={currentPage === 1}
                            className="px-3 py-1 border rounded-md text-sm disabled:opacity-50"
                        >
                            上一页
                        </button>
                        <span className="text-sm text-gray-700">
                            第 {currentPage} 页
                        </span>
                        <button
                            onClick={() => setCurrentPage(prev => prev + 1)}
                            disabled={currentPage * pageSize >= total}
                            className="px-3 py-1 border rounded-md text-sm disabled:opacity-50"
                        >
                            下一页
                        </button>
                    </div>
                </div>

                {/* 添加/编辑菜品弹窗 */}
                {isModalOpen && (
                    <div className="fixed inset-0 bg-gray-500 bg-opacity-75 flex items-center justify-center">
                        <div className="bg-white rounded-lg p-8 max-w-md w-full">
                            <h3 className="text-lg font-medium text-gray-900 mb-4">
                                {editingProduct ? "编辑菜品" : "添加菜品"}
                            </h3>
                            <form onSubmit={handleSubmit} className="space-y-4">
                                <div>
                                    <label className="block text-sm font-medium text-gray-700">分类ID</label>
                                    <input
                                        type="number"
                                        name="category_id"
                                        value={formData.category_id}
                                        onChange={handleChange}
                                        className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm p-2"
                                        required
                                    />
                                </div>
                                <div>
                                    <label className="block text-sm font-medium text-gray-700">菜品名称</label>
                                    <input
                                        type="text"
                                        name="name"
                                        value={formData.name}
                                        onChange={handleChange}
                                        className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm p-2"
                                        required
                                    />
                                </div>
                                <div>
                                    <label className="block text-sm font-medium text-gray-700">价格</label>
                                    <input
                                        type="number"
                                        name="price"
                                        value={formData.price}
                                        onChange={handleChange}
                                        className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm p-2"
                                        step="0.01"
                                        required
                                    />
                                </div>
                                <div>
                                    <label className="block text-sm font-medium text-gray-700">描述</label>
                                    <textarea
                                        name="description"
                                        value={formData.description}
                                        onChange={handleChange}
                                        className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm p-2"
                                        rows="3"
                                        required
                                    />
                                </div>
                                <div>
                                    <label className="block text-sm font-medium text-gray-700">库存</label>
                                    <input
                                        type="number"
                                        name="stock"
                                        value={formData.stock}
                                        onChange={handleChange}
                                        className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm p-2"
                                        required
                                    />
                                </div>
                                <div>
                                    <label className="block text-sm font-medium text-gray-700">状态</label>
                                    <select
                                        name="status"
                                        value={formData.status}
                                        onChange={handleChange}
                                        className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm p-2"
                                    >
                                        <option value={1}>上架</option>
                                        <option value={0}>下架</option>
                                    </select>
                                </div>
                                <div>
                                    <label className="block text-sm font-medium text-gray-700">图片URL</label>
                                    <input
                                        type="url"
                                        name="image"
                                        value={formData.image}
                                        onChange={handleChange}
                                        className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm p-2"
                                    />
                                </div>
                                <div className="flex justify-end space-x-4 mt-6">
                                    <button
                                        type="button"
                                        onClick={() => {
                                            setIsModalOpen(false);
                                            setEditingProduct(null);
                                        }}
                                        className="px-4 py-2 border border-gray-300 rounded-md text-sm font-medium text-gray-700 hover:bg-gray-50"
                                    >
                                        取消
                                    </button>
                                    <button
                                        type="submit"
                                        disabled={isLoading}
                                        className="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 disabled:bg-indigo-300"
                                    >
                                        {isLoading ? "保存中..." : "保存"}
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
};

export default ProductManagementPage; 