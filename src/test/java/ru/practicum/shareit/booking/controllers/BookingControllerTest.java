package ru.practicum.shareit.booking.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.Booker;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.ItemShortForResponse;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.common.CommonService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.time.LocalDateTime;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    CommonService commonService;

    @Autowired
    private MockMvc mvc;

    private static BookingDto bookingDto1;

    @BeforeAll
    static void beforeAll() {

        bookingDto1 = new BookingDto();
        bookingDto1.setStatus(BookingStatus.WAITING);
        bookingDto1.setId(1);
        bookingDto1.setBooker(new Booker(1));
        bookingDto1.setItem(new ItemShortForResponse(1));
        bookingDto1.setStart(LocalDateTime.now().plusDays(1));
        bookingDto1.setEnd(LocalDateTime.now().plusDays(2));

    }

    @Test
    void addBooking() throws Exception {

        when(commonService.addBooking(any(), anyInt()))
                .thenReturn(bookingDto1);

        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(bookingDto1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto1.getId()), Integer.class))
                .andExpect(jsonPath("$.status", is("WAITING")));

    }

    @Test
    void approveBooking() throws Exception {

        when(commonService.approveBooking(anyInt(), anyInt(), anyBoolean()))
                .thenReturn(bookingDto1);

        mvc.perform(patch("/bookings/1")
                        .header("X-Sharer-User-Id", 1)
                        .param("approved", "true")
                        .content(mapper.writeValueAsString(bookingDto1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto1.getId()), Integer.class))
                .andExpect(jsonPath("$.status", is("WAITING")));

    }

    @Test
    void getBookingById() throws Exception {

        when(commonService.getBookingById(anyInt(), anyInt()))
                .thenReturn(bookingDto1);

        mvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(bookingDto1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto1.getId()), Integer.class))
                .andExpect(jsonPath("$.status", is("WAITING")));

    }

    @Test
    void getAllBookingsByBookerIdDesc() throws Exception {

        when(commonService.getAllBookingsByBookerIdDesc(anyInt(), anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingDto1));

        mvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "20")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()", is(1)));

    }

    @Test
    void getAllBookingsByItemOwnerId() throws Exception {

        when(commonService.getAllBookingsByItemOwnerId(anyInt(), anyString()))
                .thenReturn(List.of(bookingDto1));

        mvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "ALL")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()", is(1)));

    }
}