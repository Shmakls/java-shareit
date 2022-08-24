package ru.practicum.shareit.item.controllers;

import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.common.CommonService;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForGetItems;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.model.ItemRequest;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    CommonService commonService;

    @MockBean
    ItemService itemService;

    @Autowired
    private MockMvc mvc;

    private static ItemDto itemDto1;

    private static Comment comment1;

    private static CommentDto commentDto1;

    private static ItemDtoForGetItems itemDtoForGetItems1;

    @BeforeAll
    static void beforeAll() {

        itemDto1 = new ItemDto();
        itemDto1.setId(1);
        itemDto1.setAvailable(true);
        itemDto1.setName("item1");
        itemDto1.setDescription("item1description");
        itemDto1.setRequestId(1);
        itemDto1.setOwner(1);

        comment1 = new Comment();
        comment1.setText("comment1text");
        comment1.setItemId(1);
        comment1.setId(1);
        comment1.setAuthorId(1);
        comment1.setCreated(LocalDateTime.now());

        itemDtoForGetItems1 = new ItemDtoForGetItems();
        itemDtoForGetItems1.setAvailable(true);
        itemDtoForGetItems1.setDescription("item1description");
        itemDtoForGetItems1.setName("item1");
        itemDtoForGetItems1.setId(1);

        commentDto1 = new CommentDto();
        commentDto1.setText("comment1text");
        commentDto1.setId(1);
        commentDto1.setAuthorId(1);
        commentDto1.setCreated(LocalDateTime.now());
    }

    @Test
    void addItem() throws Exception {

        when(commonService.addItem(anyInt(), any()))
                .thenReturn(itemDto1);

        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(itemDto1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto1.getId()), Integer.class))
                .andExpect(jsonPath("$.available", is(true)))
                .andExpect(jsonPath("$.name", is(itemDto1.getName())))
                .andExpect(jsonPath("$.description", is(itemDto1.getDescription())));

    }

    @Test
    void updateItem() throws Exception {

        when(itemService.updateItem(anyInt(), any(), anyInt()))
                .thenReturn(itemDto1);

        mvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(itemDto1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto1.getId()), Integer.class))
                .andExpect(jsonPath("$.available", is(true)))
                .andExpect(jsonPath("$.name", is(itemDto1.getName())))
                .andExpect(jsonPath("$.description", is(itemDto1.getDescription())));

    }

    @Test
    void getItemById() throws Exception {

        when(commonService.getItemById(anyInt(), anyInt()))
                .thenReturn(itemDtoForGetItems1);

        mvc.perform(get("/items/1").header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(itemDtoForGetItems1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDtoForGetItems1.getId()), Integer.class))
                .andExpect(jsonPath("$.available", is(true)))
                .andExpect(jsonPath("$.name", is(itemDtoForGetItems1.getName())))
                .andExpect(jsonPath("$.description", is(itemDtoForGetItems1.getDescription())));



    }

    @Test
    void getItemsListByOwnerId() throws Exception {

        when(commonService.getItemsListByOwnerId(anyInt(), anyInt(), anyInt()))
                .thenReturn(List.of(itemDtoForGetItems1));

        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1)
                        .param("from", "0")
                        .param("size", "20")
                        .content(mapper.writeValueAsString(itemDtoForGetItems1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()", is(1)));

    }

    @Test
    void searchItemForRentByText() throws Exception {

        when(itemService.searchItemForRentByText(anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(itemDto1));

        mvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", 1)
                        .param("text", "textForSearch")
                        .param("from", "0")
                        .param("size", "20")
                        .content(mapper.writeValueAsString(itemDto1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()", is(1)));

    }

    @Test
    void addCommentToItem() throws Exception {

        when(commonService.addComment(any(), anyInt(), anyInt()))
                .thenReturn(commentDto1);

        mvc.perform(post("/items/1/comment")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(comment1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentDto1.getId()), Integer.class))
                .andExpect(jsonPath("$.text", is(commentDto1.getText())));

    }
}