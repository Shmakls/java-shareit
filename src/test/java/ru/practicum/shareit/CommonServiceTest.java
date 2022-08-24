package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.exceptions.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.common.CommonService;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForGetItems;
import ru.practicum.shareit.item.exceptions.BookingForCommentNotFoundException;
import ru.practicum.shareit.item.exceptions.ItemNotFoundException;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.exceptions.InvalidParametersException;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CommonServiceTest {

    private final CommonService commonService;

    private final UserService userService;

    private static UserDto createUserDto(String email, String name) {

        UserDto userDto = new UserDto();
        userDto.setName(name);
        userDto.setEmail(email);

        return userDto;

    }

    private static ItemDto createItemDto(String description, String name, boolean available) {

        ItemDto itemDto = new ItemDto();
        itemDto.setDescription(description);
        itemDto.setName(name);
        itemDto.setAvailable(available);

        return itemDto;

    }

    private static Booking createBookingDto(BookingStatus status, Integer itemId, Integer bookerId,
                                               LocalDateTime start, LocalDateTime end) {

        Booking booking = new Booking();
        booking.setStatus(status);
        booking.setItemId(itemId);
        booking.setBookerId(bookerId);
        booking.setStart(start);
        booking.setEnd(end);

        return booking;

    }

    private static ItemRequest createItemRequest(String description) {

        ItemRequest itemRequest = new ItemRequest();

        itemRequest.setDescription(description);

        return itemRequest;

    }

    private static Comment createComment(String text) {

        Comment comment = new Comment();
        comment.setText(text);

        return comment;
    }

    @Test
    void shouldBeAddItem() {

        UserDto userDto1 = createUserDto("user1@email", "user1");

        ItemDto itemDto1 = createItemDto("item1Description", "item1", true);

        ItemDto finalItemDto = itemDto1;
        final UserNotFoundException e = assertThrows(
                UserNotFoundException.class,
                () -> commonService.addItem(1, finalItemDto)
        );

        assertEquals("Пользователь не существует или некорректный id", e.getMessage());

        userService.addUser(userDto1);

        itemDto1 = commonService.addItem(1, itemDto1);

        assertEquals(1, itemDto1.getId());

    }

    @Test
    void shouldBeGetItemsListByOwnerId() throws InterruptedException {

        ItemDto itemDto1 = createItemDto("item1Description", "item1", true);
        ItemDto itemDto2 = createItemDto("item2Description", "item2", true);
        ItemDto itemDto3 = createItemDto("item3Description", "item3", true);

        UserDto userDto1 = createUserDto("user1@email", "user1");
        UserDto userDto2 = createUserDto("user2@email", "user2");
        UserDto userDto3 = createUserDto("user3@email", "user3");

        userService.addUser(userDto1);
        userService.addUser(userDto2);
        userService.addUser(userDto3);

        itemDto1 = commonService.addItem(1, itemDto1);
        itemDto2 = commonService.addItem(1, itemDto2);
        itemDto3 = commonService.addItem(2, itemDto3);

        Booking bookingLast = createBookingDto(BookingStatus.APPROVED, 1, 3,
                LocalDateTime.now().plusSeconds(1),LocalDateTime.now().plusSeconds(3));

        Booking bookingNext = createBookingDto(BookingStatus.APPROVED, 1, 3,
                LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2));

        BookingDto bookingDtoLast = commonService.addBooking(bookingLast, 3);
        BookingDto bookingDtoNext = commonService.addBooking(bookingNext, 3);

        Thread.sleep(4000);

        var items = commonService.getItemsListByOwnerId(-1, 0, 20);

        assertEquals(3, items.size());

        items = commonService.getItemsListByOwnerId(1, 0, 20);

        assertEquals(2, items.size());

    }

    @Test
    void shouldBeGetItemByIdAndTestComment() throws InterruptedException {

        Comment comment = createComment("comment1ToItem1");

        final UserNotFoundException userNotFoundException = assertThrows(
                UserNotFoundException.class,
                () -> commonService.addComment(comment, 1, 1)
        );

        assertEquals("Такого пользователя в базе нет", userNotFoundException.getMessage());

        UserDto userDto1 = createUserDto("user1@email", "user1");
        userService.addUser(userDto1);

        final ItemNotFoundException itemNotFoundException = assertThrows(
                ItemNotFoundException.class,
                () -> commonService.addComment(comment, 1, 1)
        );

        assertEquals("Такой вещи в базе нет", itemNotFoundException.getMessage());

        ItemDto itemDto1 = createItemDto("item1Description", "item1", true);

        itemDto1 = commonService.addItem(1, itemDto1);

        final BookingForCommentNotFoundException bookingForCommentNotFoundException = assertThrows(
                BookingForCommentNotFoundException.class,
                () -> commonService.addComment(comment, 1, 1)
        );

        assertEquals("Бронирования для запрошенной вещи не было", bookingForCommentNotFoundException.getMessage());

        UserDto userDto2 = createUserDto("user2@email", "user2");
        userService.addUser(userDto2);

        Booking bookingLast = createBookingDto(BookingStatus.APPROVED, 1, 2,
                LocalDateTime.now().plusSeconds(1),LocalDateTime.now().plusSeconds(3));

        BookingDto bookingDtoLast = commonService.addBooking(bookingLast, 2);

        Thread.sleep(4000);

        final NoAccessRightsException noAccessRightsException = assertThrows(
                NoAccessRightsException.class,
                () -> commonService.addComment(comment, 1, 1)
        );

        assertEquals("Пользователь не брал вещь в аренду", noAccessRightsException.getMessage());

        CommentDto commentDto = commonService.addComment(comment, 1, 2);

        assertEquals(1, commentDto.getId());

        ItemDtoForGetItems resultWithoutBooking = commonService.getItemById(1, 2);

        assertNull(resultWithoutBooking.getLastBooking());

        ItemDtoForGetItems resultWithLastBooking = commonService.getItemById(1, 1);

        assertNotNull(resultWithLastBooking.getLastBooking());

    }

    @Test
    void bookingFunctionalTest() {

        ItemDto itemDto1 = createItemDto("item1Description", "item1", true);
        ItemDto itemDto2 = createItemDto("item2Description", "item2", false);
        ItemDto itemDto3 = createItemDto("item3Description", "item3", true);

        UserDto userDto1 = createUserDto("user1@email", "user1");
        UserDto userDto2 = createUserDto("user2@email", "user2");
        UserDto userDto3 = createUserDto("user3@email", "user3");

        userDto1 = userService.addUser(userDto1);
        userDto2 = userService.addUser(userDto2);
        userDto3 = userService.addUser(userDto3);

        itemDto1 = commonService.addItem(1, itemDto1);
        itemDto2 = commonService.addItem(1, itemDto2);
        itemDto3 = commonService.addItem(1, itemDto3);

        //Тестируем все ошибки при создании бронирования
        Booking bookingForItem1FromUser2 = createBookingDto(BookingStatus.APPROVED, 1, 2,
                LocalDateTime.now().plusSeconds(1),LocalDateTime.now().plusSeconds(3));

        final UserNotFoundException userNotFoundException = assertThrows(
                UserNotFoundException.class,
                () -> commonService.addBooking(bookingForItem1FromUser2, 99)
        );

        assertEquals("Такого пользователя в базе нет", userNotFoundException.getMessage());

        Booking bookingWithWrongItemId = createBookingDto(BookingStatus.APPROVED, 99, 2,
                LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(3));

        final ItemNotFoundException itemNotFoundException = assertThrows(
                ItemNotFoundException.class,
                () -> commonService.addBooking(bookingWithWrongItemId, 2)
        );

        assertEquals("Такой вещи в базе нет", itemNotFoundException.getMessage());

        Booking bookingForFalseAvailableStatus = createBookingDto(BookingStatus.APPROVED, 2, 2,
                LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(3));

        final IncorrectItemStatusForBookingException incorrectItemStatusForBookingException = assertThrows(
                IncorrectItemStatusForBookingException.class,
                () -> commonService.addBooking(bookingForFalseAvailableStatus, 2)
        );

        assertEquals("Вещь недопустна для бронирования", incorrectItemStatusForBookingException.getMessage());

        Booking bookingForItem1FromOwner = createBookingDto(BookingStatus.APPROVED, 1, 1,
                LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(3));

        final BookingNotFoundException bookingNotFoundException = assertThrows(
                BookingNotFoundException.class,
                () -> commonService.addBooking(bookingForItem1FromOwner, 1)
        );

        assertEquals("Бронирование нельзя создавать владельцем этой вещи", bookingNotFoundException.getMessage());

        //Тестируем getBookingById
        final BookingNotFoundException bookingNotFoundExceptionWrongBookingId = assertThrows(
                BookingNotFoundException.class,
                () -> commonService.getBookingById(1, 99)
        );

        assertEquals("Такого бронирования в базе нет", bookingNotFoundExceptionWrongBookingId.getMessage());

        BookingDto bookingDtoForItem1FromUser2 = commonService.addBooking(bookingForItem1FromUser2, 2);

        final NoAccessRightsException noAccessRightsException = assertThrows(
                NoAccessRightsException.class,
                () -> commonService.getBookingById(99, 1)
        );

        assertEquals("Нет прав доступа к бронированию", noAccessRightsException.getMessage());

        bookingDtoForItem1FromUser2 = commonService.getBookingById(2, 1);

        assertEquals("item1", bookingDtoForItem1FromUser2.getItem().getName());

        //Тестируем approveBooking
        final NoAccessRightsException noAccessRightsExceptionFromNotOwner = assertThrows(
                NoAccessRightsException.class,
                () -> commonService.approveBooking(99, 1, true)
        );

        assertEquals("Нет прав доступа к бронированию", noAccessRightsExceptionFromNotOwner.getMessage());

        bookingDtoForItem1FromUser2 = commonService.approveBooking(1, 1, true);

        assertEquals(BookingStatus.APPROVED, bookingDtoForItem1FromUser2.getStatus());

        final RepeatRequestException repeatRequestException = assertThrows(
                RepeatRequestException.class,
                () -> commonService.approveBooking(1, 1, true)
        );

        assertEquals("Запрос уже был обработан владельцем вещи", repeatRequestException.getMessage());

        Booking bookingForItem3FromUser3 = createBookingDto(BookingStatus.APPROVED, 3, 3,
                LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(3));

        BookingDto bookingDtoForItem3FromUser3 = commonService.addBooking(bookingForItem3FromUser3, 3);

        bookingDtoForItem3FromUser3 = commonService.approveBooking(1, 2, false);

        assertEquals(BookingStatus.REJECTED, bookingDtoForItem3FromUser3.getStatus());

    }

    @Test
    void testGetBookingsFunctional() throws InterruptedException {

        ItemDto itemDto1 = createItemDto("item1Description", "item1", true);
        ItemDto itemDto2 = createItemDto("item2Description", "item2", true);
        ItemDto itemDto3 = createItemDto("item3Description", "item3", true);

        UserDto userDto1 = createUserDto("user1@email", "user1");
        UserDto userDto2 = createUserDto("user2@email", "user2");
        UserDto userDto3 = createUserDto("user3@email", "user3");

        userDto1 = userService.addUser(userDto1);
        userDto2 = userService.addUser(userDto2);
        userDto3 = userService.addUser(userDto3);

        itemDto1 = commonService.addItem(1, itemDto1);
        itemDto2 = commonService.addItem(1, itemDto2);
        itemDto3 = commonService.addItem(1, itemDto3);

        //Тест вылета исключения при получении бронирований по неверному id букера
        final UserNotFoundException userNotFoundException = assertThrows(
                UserNotFoundException.class,
                () -> commonService.getAllBookingsByBookerIdDesc(99, "PAST", 0, 20)
        );

        assertEquals("Такого пользователя в базе нет", userNotFoundException.getMessage());

        Booking bookingPast = createBookingDto(BookingStatus.APPROVED, 1, 3,
                LocalDateTime.now().plusSeconds(1), LocalDateTime.now().plusSeconds(3));

        Booking bookingCurrent = createBookingDto(BookingStatus.APPROVED, 1, 3,
                LocalDateTime.now().plusSeconds(1), LocalDateTime.now().plusDays(1));

        Booking bookingFuture = createBookingDto(BookingStatus.APPROVED, 2, 3,
                LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(4));

        List<BookingDto> bookingsEmpty = commonService.getAllBookingsByBookerIdDesc(3, "ALL", 0, 20);

        assertEquals(0, bookingsEmpty.size());

        BookingDto bookingDtoPast = commonService.addBooking(bookingPast, 3);
        BookingDto bookingDtoCurrent = commonService.addBooking(bookingCurrent, 3);
        BookingDto bookingDtoFuture = commonService.addBooking(bookingFuture, 3);

        Thread.sleep(4000);

        List<BookingDto> bookingsCurrent = commonService.getAllBookingsByBookerIdDesc(3, "CURRENT", 0, 20);

        assertEquals(1, bookingsCurrent.size());

        List<BookingDto> bookingsPast = commonService.getAllBookingsByBookerIdDesc(3, "PAST", 0, 20);

        assertEquals(1, bookingsPast.size());

        List<BookingDto> bookingsFuture = commonService.getAllBookingsByBookerIdDesc(3, "FUTURE", 0, 20);

        assertEquals(1, bookingsFuture.size());

        commonService.approveBooking(1, 3, false);

        List<BookingDto> bookingsWaiting = commonService.getAllBookingsByBookerIdDesc(3, "WAITING", 0, 20);

        assertEquals(2, bookingsWaiting.size());

        List<BookingDto> bookingsRejected = commonService.getAllBookingsByBookerIdDesc(3, "REJECTED", 0, 20);

        assertEquals(1, bookingsRejected.size());

        List<BookingDto> bookingsAll = commonService.getAllBookingsByBookerIdDesc(3, "ALL", 0, 20);

        assertEquals(3, bookingsAll.size());

        final IncorrectStateException incorrectStateException = assertThrows(
                IncorrectStateException.class,
                () -> commonService.getAllBookingsByBookerIdDesc(3, "WHERE", 0, 20)
        );

        assertEquals("Unknown state: WHERE", incorrectStateException.getMessage());

        //Тестируем метод getAllBookingsByItemOwnerId

        final UserNotFoundException userNotFoundExceptionByGetAll = assertThrows(
                UserNotFoundException.class,
                () -> commonService.getAllBookingsByItemOwnerId(99, "ALL")
        );

        assertEquals("Такого пользователя в базе нет", userNotFoundExceptionByGetAll.getMessage());

        List<BookingDto> noBookingsForUser = commonService.getAllBookingsByItemOwnerId(3, "ALL");

        assertEquals(0, noBookingsForUser.size());

        List<BookingDto> bookingsByUser1 = commonService.getAllBookingsByItemOwnerId(1, "ALL");

        assertEquals(3, bookingsByUser1.size());

    }

    @Test
    void testRequestFunctional() {

        ItemDto itemDto1 = createItemDto("item1Description", "item1", true);
        ItemDto itemDto2 = createItemDto("item2Description", "item2", true);
        ItemDto itemDto3 = createItemDto("item3Description", "item3", true);

        UserDto userDto1 = createUserDto("user1@email", "user1");
        UserDto userDto2 = createUserDto("user2@email", "user2");
        UserDto userDto3 = createUserDto("user3@email", "user3");

        userDto1 = userService.addUser(userDto1);
        userDto2 = userService.addUser(userDto2);
        userDto3 = userService.addUser(userDto3);

        itemDto1 = commonService.addItem(1, itemDto1);
        itemDto2 = commonService.addItem(1, itemDto2);
        itemDto3 = commonService.addItem(1, itemDto3);

        ItemRequest itemRequest1 = createItemRequest("descriptionItemRequest1");

        //Тестируем добавление запроса
        final UserNotFoundException userNotFoundException = assertThrows(
                UserNotFoundException.class,
                () -> commonService.addItemRequest(itemRequest1, 99)
        );

        assertEquals("Такого пользователя в базе нет", userNotFoundException.getMessage());

        ItemRequestDto itemRequestDto1 = commonService.addItemRequest(itemRequest1, 3);

        assertEquals(1, itemRequestDto1.getId());

        //Тестируем получение запроса по id
        final UserNotFoundException userNotFoundExceptionByGetId = assertThrows(
                UserNotFoundException.class,
                () -> commonService.getItemRequestById(99, 1)
        );

        assertEquals("Такого пользователя в базе нет", userNotFoundExceptionByGetId.getMessage());

        ItemRequestDto itemRequestDtoById = commonService.getItemRequestById(2, 1);

        assertEquals(1, itemRequestDtoById.getId());

        //Тестируем findAllRequestsByRequestorId
        final UserNotFoundException userNotFoundExceptionByFindAllByRequestorId = assertThrows(
                UserNotFoundException.class,
                () -> commonService.findAllRequestsByRequestorId(99)
        );

        assertEquals("Такого пользователя в базе нет", userNotFoundExceptionByFindAllByRequestorId.getMessage());

        ItemRequest itemRequest2 = createItemRequest("descriptionItemRequest2");

        ItemRequestDto itemRequestDto2 = commonService.addItemRequest(itemRequest2, 3);

        List<ItemRequestDto> itemRequestsByRequestorId = commonService.findAllRequestsByRequestorId(3);

        assertEquals(2, itemRequestsByRequestorId.size());

        //Тестируем findAllRequestsByPages
        final UserNotFoundException userNotFoundExceptionByFindAll = assertThrows(
                UserNotFoundException.class,
                () -> commonService.findAllRequestsByPages(99, 0, 20)
        );

        assertEquals("Такого пользователя в базе нет", userNotFoundExceptionByFindAll.getMessage());

        final InvalidParametersException invalidParametersException1 = assertThrows(
                InvalidParametersException.class,
                () -> commonService.findAllRequestsByPages(1, -1, 20)
        );

        assertEquals("Параметр size или from некорректный", invalidParametersException1.getMessage());

        final InvalidParametersException invalidParametersException2 = assertThrows(
                InvalidParametersException.class,
                () -> commonService.findAllRequestsByPages(1, 0, 0)
        );

        assertEquals("Параметр size или from некорректный", invalidParametersException2.getMessage());

        List<ItemRequestDto> itemRequestsByFindAll = commonService.findAllRequestsByPages(1, 0, 20);

        assertEquals(2, itemRequestsByFindAll.size());

        ItemDto itemDto4 = createItemDto("item4Description", "item4", true);
        itemDto4.setRequestId(1);

        itemDto4 = commonService.addItem(1, itemDto4);

        ItemRequestDto itemRequestDtoForItem4 = commonService.getItemRequestById(1, 1);

        assertEquals(1, itemRequestDtoForItem4.getItems().size());

    }

}