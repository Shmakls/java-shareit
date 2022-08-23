package ru.practicum.shareit.requests.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.common.CommonService;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.model.ItemRequest;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTest {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    CommonService commonService;

    @Autowired
    private MockMvc mvc;

    private static ItemRequest itemRequest1;
    private static ItemRequest itemRequest2;
    private static ItemRequestDto itemRequestDto1;

    @BeforeAll
    static void beforeAll() {

        itemRequest1 = new ItemRequest();
        itemRequest1.setDescription("itemRequest1");

        itemRequest2 = new ItemRequest();
        itemRequest2.setDescription("itemRequest2");

        itemRequestDto1 = new ItemRequestDto();
        itemRequestDto1.setDescription("itemRequest1");

    }

    @Test
    void addItemRequest() throws Exception {

        when(commonService.addItemRequest(any(), anyInt()))
                .thenReturn(itemRequestDto1);

        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(itemRequest1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDto1.getId()), Integer.class))
                .andExpect(jsonPath("$.description", is(itemRequestDto1.getDescription())));

    }

    @Test
    void findAllRequestsByUser() throws Exception {

        when(commonService.findAllRequestsByRequestorId(anyInt()))
                .thenReturn(List.of(itemRequestDto1));

        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()", is(1)));


    }

    @Test
    void findAllRequestsByPages() {
    }

    @Test
    void findRequestById() {
    }
}