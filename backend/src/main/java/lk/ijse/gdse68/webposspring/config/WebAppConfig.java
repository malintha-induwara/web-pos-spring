package lk.ijse.gdse68.webposspring.config;

import jakarta.servlet.annotation.MultipartConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@ComponentScan(basePackages = "lk.ijse.gdse68.webposspring")
@EnableWebMvc
@EnableJpaRepositories(basePackages = "lk.ijse.gdse68.webposspring")
@EnableTransactionManagement
@MultipartConfig(
        location = "/tmp",
        fileSizeThreshold = 1024 * 1024 * 2,
        maxFileSize = 1024 * 1024 * 10,
        maxRequestSize = 1024 * 1024 * 50
)
public class WebAppConfig {
}

