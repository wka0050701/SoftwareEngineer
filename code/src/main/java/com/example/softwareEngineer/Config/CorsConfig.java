package com.example.softwareEngineer.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        // 创建CORS配置对象
        CorsConfiguration config = new CorsConfiguration();

        // 允许所有来源（生产环境建议指定具体域名，如"http://localhost:3000"）
        config.addAllowedOriginPattern("*");

        // 允许所有请求头
        config.addAllowedHeader("*");

        // 允许所有请求方法（GET、POST、PUT、DELETE等）
        config.addAllowedMethod("*");

        // 允许携带Cookie（若前后端需要共享Cookie，需开启此项并关闭`addAllowedOriginPattern("*")`，改用具体域名）
        // config.setAllowCredentials(true);

        // 跨域预处理请求的缓存时间（单位：秒）
        config.setMaxAge(3600L);

        // 创建CORS配置源
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 配置匹配路径（`/**`表示所有接口）
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}
