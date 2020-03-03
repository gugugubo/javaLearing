package com.gcb.learning.mybatisplus.security.utils;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class JwtTokenUtilTest {

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Test
    void getClaimsFromToken() {
        Claims claimsFromToken = jwtTokenUtil.getClaimsFromToken("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImNyZWF0ZWQiOjE1ODEzODk5MTUzNDksImV4cCI6MTU4MTk5NDcxNX0.thgGNwMReitZCWgtemSQpxvjf_fwH_tbPEMjEeAX5lqWdbwfaqHBpHDUBAyYpfow6nLkx4xrmOyud75p4Pa-iw");
        System.out.println(claimsFromToken.toString());
    }
}