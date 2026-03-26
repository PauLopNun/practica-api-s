package com.exampleinyection.clase2parte2;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GFTFormacionTests {

    @Test
    void contextLoads() {
    }

    @Test
    void main() {
        GFTFormacion.main(new String[]{"--server.port=0", "--spring.main.web-application-type=none"});
    }

}
