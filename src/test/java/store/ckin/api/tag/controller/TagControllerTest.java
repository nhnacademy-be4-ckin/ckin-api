package store.ckin.api.tag.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import store.ckin.api.tag.service.impl.TagServiceImpl;

/**
 * description
 *
 * @author 김준현
 * @version 2024. 02. 17
 */
@ExtendWith(MockitoExtension.class)
class TagControllerTest {
    private MockMvc mockMvc;
    @InjectMocks
    private TagController tagController;
    @Mock
    private TagServiceImpl tagService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(tagController).build();
    }

    @Test
    void getAllTagList() throws Exception{
        mockMvc.perform(get("/api/tags"))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    void saveTag() {
    }

    @Test
    void updateTag() {
    }

    @Test
    void deleteTag() {
    }
}