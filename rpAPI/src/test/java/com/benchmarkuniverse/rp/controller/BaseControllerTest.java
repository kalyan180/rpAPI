package com.benchmarkuniverse.rp.controller;

import org.junit.Before;

import com.benchmarkuniverse.rp.filters.JwtRequestFilter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
@WebAppConfiguration
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public abstract class BaseControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Value("${jwt.auth.token}")
    private String jwtToken;

    @Value("${jwt.auth.invalid.token}")
    private String invalidToken;

    @Autowired
    protected WebApplicationContext webApplicationContext;

    protected MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));
    protected String api_version = "/";

    @Autowired
    private JwtRequestFilter filter;

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).addFilter(filter)
                .build();
    }

    protected String buildRequestBody(Object bodyObj) throws JsonProcessingException {
        String jsonString = new ObjectMapper().writeValueAsString(bodyObj);
        return jsonString;
    }

    protected ResultActions performBasicGet(String path) throws Exception {
        return this.mockMvc.perform(get(path).header("Authorization", jwtToken));
    }

    protected ResultActions performBasicGetWithInvalidAuthHeader(String path) throws Exception {
        return this.mockMvc.perform(get(path).header("Authorization", "Invalid token"));
    }

    protected ResultActions performBasicPostWithReqObj(String path, Object bodyObj) throws Exception {
        return this.mockMvc.perform(
                post(path).header(HttpHeaders.AUTHORIZATION, jwtToken)
                        .content(buildRequestBody(bodyObj))
                        .contentType(contentType)
        );
    }

    protected ResultActions performBasicPostWithInvalidAuthHeader(String path,Object bodyObj) throws Exception {
        return this.mockMvc.perform(post(path).header("Authorization", "Invalid token")
        .content(buildRequestBody(bodyObj)).contentType(contentType));
    }

}
