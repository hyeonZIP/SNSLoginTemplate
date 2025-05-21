package zip.hyeon.snslogintemplate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SnsLoginTemplateApplication {

    public static void main(String[] args) {
        SpringApplication.run(SnsLoginTemplateApplication.class, args);
    }

}
