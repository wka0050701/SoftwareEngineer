import Mock from 'mockjs';

// 只在开发环境中启用 mock
if (process.env.NODE_ENV === 'development') {
    // 设置延迟时间
    Mock.setup({
        timeout: '200-600'
    });

    // 注册接口
    Mock.mock(new RegExp('/api/auth/register'), 'post', (options) => {
        console.log('Mock拦截到注册请求:', options);
        const { phone, password, nickname, default_address } = JSON.parse(options.body);
        
        // 模拟手机号已被注册的情况
        if (phone === '12345678900') {
            return {
                code: 0,
                message: "该手机号已被注册",
                data: null
            };
        }
        
        // 模拟昵称不能为 admin 的情况
        if (nickname === 'admin') {
            return {
                code: 0,
                message: "昵称不能为 'admin'",
                data: null
            };
        }

        // 模拟注册成功
        return {
            code: 1,
            message: "注册成功",
            data: {
                user_id: Mock.Random.integer(1, 1000)
            }
        };
    });

    // 登录接口
    Mock.mock(new RegExp('/api/auth/login'), 'post', (options) => {
        console.log('Mock拦截到登录请求:', options);
        const { phone, password } = JSON.parse(options.body);
        
        // 模拟登录成功
        return {
            code: 1,
            message: "登录成功",
            data: {
                user_id: phone === '12345678900' ? 1 : Mock.Random.integer(2, 1000)
            }
        };
    });

    // 找回密码接口
    Mock.mock(new RegExp('/api/auth/findpassword'), 'get', (options) => {
        console.log('Mock拦截到找回密码请求:', options);
        const { phone } = JSON.parse(options.body);
        
        // 模拟找回密码成功
        return {
            code: 1,
            message: "成功",
            data: {
                password: "securePassword123"
            }
        };
    });

    // 添加一个测试接口
    Mock.mock(new RegExp('/api/test'), 'get', () => {
        console.log('Mock拦截到测试请求');
        return {
            code: 1,
            message: "测试成功",
            data: {
                test: "mock is working"
            }
        };
    });

    console.log('Mock.js 已初始化（开发环境）');
}

export default Mock; 