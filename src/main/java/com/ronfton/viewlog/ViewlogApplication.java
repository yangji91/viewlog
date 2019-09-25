package com.ronfton.viewlog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class ViewlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(ViewlogApplication.class, args);
    }

}
