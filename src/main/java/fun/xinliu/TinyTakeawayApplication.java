package fun.xinliu;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@SpringBootApplication
@ServletComponentScan
@EnableTransactionManagement
public class TinyTakeawayApplication {

    public static void main(String[] args) {
        SpringApplication.run(TinyTakeawayApplication.class, args);
        log.info("SpringBoot Application is running...");
    }

}
