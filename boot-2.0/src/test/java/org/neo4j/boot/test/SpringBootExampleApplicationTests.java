package org.neo4j.boot.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpringBootExampleApplicationTests {

    @Resource
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
	public void test_create_user() throws Exception {
        mockMvc.perform(post("/test")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"email\":\"aa@bb.com\"}"))
            .andExpect(status().isOk())
            .andExpect(content().json("{\n" +
                "  \"firstName\": null,\n" +
                "  \"lastName\": null,\n" +
                "  \"email\": \"aa@bb.com\"\n" +
                "}"));
    }
}
