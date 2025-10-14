package com.isocial.minisocialbe; // Đảm bảo package này đúng

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.awt.Desktop;
import java.net.URI;

@Configuration
public class SwaggerAutoOpenConfig {

    @Value("${server.port:8080}")
    private String port;

    @Value("${server.servlet.context-path:}")
    private String contextPath;

    @Value("${springdoc.swagger-ui.path:/swagger-ui.html}")
    private String swaggerPath;

    @Bean
    public CommandLineRunner swaggerAutoOpenRunner() {
        return args -> {
            try {
                String url = "http://localhost:" + port + contextPath + swaggerPath;

                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    if (System.getProperty("java.awt.headless", "false").equals("false")) {
                        Desktop.getDesktop().browse(new URI(url));
                        System.out.println("✅ Tự động mở Swagger UI tại: " + url);
                    } else {
                        System.out.println("⚠️ Môi trường Headless (Server) được phát hiện. Không tự động mở trình duyệt.");
                    }
                } else {
                    System.err.println("❌ Cảnh báo: Môi trường không hỗ trợ Desktop (hoặc chưa khởi động). Hãy truy cập thủ công:");
                    System.err.println("🔗 URL Swagger UI: " + url);
                }
            } catch (Exception e) {
                System.err.println("❌ Lỗi xảy ra khi cố gắng mở trình duyệt.");
                e.printStackTrace();
            }
        };
    }
}