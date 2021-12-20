package com.proyectolibreria.demo;

import com.proyectolibreria.demo.servicios.ClienteServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class DemoApplication {
 @Autowired
    private ClienteServicio clienteServicio;
 
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
@Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(clienteServicio)
                .passwordEncoder(new BCryptPasswordEncoder());

    }
}
