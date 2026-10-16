package ro.tuc.ds2020;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import java.util.TimeZone;

@SpringBootApplication
@SuppressWarnings("checkstyle:HideUtilityClassConstructor")
public class UserApplication {
    public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        SpringApplication.run(UserApplication.class, args);
    }
}
