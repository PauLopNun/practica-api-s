package com.exampleinyection.clase2parte2.config;

import com.exampleinyection.clase2parte2.model.Allergy;
import com.exampleinyection.clase2parte2.model.User;
import com.exampleinyection.clase2parte2.repository.AllergyRepository;
import com.exampleinyection.clase2parte2.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(UserRepository userRepository, AllergyRepository allergyRepository) {
        return args -> {
            for (int i = 1; i <= 30; i++) {
                User user = new User();
                user.setName("User " + i);
                user.setAge(20 + (i % 50));
                
                userRepository.save(user);

                for (int j = 1; j <= 5; j++) {
                    Allergy allergy = new Allergy();
                    allergy.setName("Allergy " + j + " del User " + i);
                    allergy.setSeverity(j);
                    allergy.setUser(user);

                    allergyRepository.save(allergy);
                }
            }
        };
    }
}

