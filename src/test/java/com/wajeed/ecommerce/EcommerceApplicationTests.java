package com.wajeed.ecommerce;

import com.wajeed.ecommerce.security.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EcommerceApplicationTests {
    @Autowired
    JwtService jwtService;

    @Test
    void contextLoads() {

    }

    @Test
    public void mapSecretKey()
    {
        System.out.println(jwtService.secretKey);
    }

}
