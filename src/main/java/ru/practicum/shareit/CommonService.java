package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.exceptions.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.BookingForItem;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForGetItems;
import ru.practicum.shareit.item.exceptions.BookingForCommentNotFoundException;
import ru.practicum.shareit.item.exceptions.ItemNotFoundException;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.service.UserService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommonService {

    private final ItemService itemService;

    private final UserService userService;

    private final BookingService bookingService;

    private final ItemMapper itemMapper;

    private final CommentMapper commentMapper;

    public ItemDto addItem(Integer userId, ItemDto itemDto) {

        if (!userService.isExists(userId) || userId == null) {

            log.info("CommonService: Пользователь не существует или некорректный id={} ", userId);

            throw new UserNotFoundException("Пользователь не существует или некорректный id");
        }

        log.info("CommonService: получен запрос на добавление вещи {} пользователя с id={} ", itemDto.getName(), userId);

        return itemService.addItem(userId, itemDto);

    }

    public List<ItemDtoForGetItems> getItemsListByOwnerId(Integer userId) {

        if (userId.equals(-1)) {

            List<ItemDtoForGetItems> itemsDtoForGetItems = itemService.findAllItems();

            for (ItemDtoForGetItems itemDtoForGetItem : itemsDtoForGetItems) {
                setLastAndNextBooking(itemDtoForGetItem);
            }

            return itemsDtoForGetItems.stream()
                    .sorted(Comparator.comparingInt(ItemDtoForGetItems::getId))
                    .collect(Collectors.toList());

        } else {

            List<Item> items = itemService.getItemsListByOwnerId(userId);

            List<ItemDtoForGetItems> itemsDtoForGetItemsByOwnerId = items
                    .stream()
                    .map(itemMapper::fromDtoToFindAll)
                    .collect(Collectors.toList());

            for (ItemDtoForGetItems itemDtoForGetItem : itemsDtoForGetItemsByOwnerId) {
                setLastAndNextBooking(itemDtoForGetItem);
            }

            return itemsDtoForGetItemsByOwnerId.stream()
                    .sorted(Comparator.comparingInt(ItemDtoForGetItems::getId))
                    .collect(Collectors.toList());

        }

    }

    private void setLastAndNextBooking(ItemDtoForGetItems itemDtoForGetItem) {

        Comparator<Booking> endComparator = (o1, o2) -> {

            if (o1.getEnd().isBefore(o2.getEnd())) {
                return -1;
            } else if (o2.getEnd().isBefore(o1.getEnd())) {
                return 1;
            } else {
                return 0;
            }

        };

        Comparator<Booking> startComparator = (o1, o2) -> {

            if (o1.getStart().isBefore(o2.getStart())) {
                return 1;
            } else if (o1.getStart().isAfter(o2.getStart())) {
                return -1;
            } else {
                return 0;
            }
        };

        List<Booking> bookings = bookingService.findBookingsByItemId(itemDtoForGetItem.getId());

        Optional<Booking> lastBooking = bookings.stream().sorted(endComparator)
                .filter(x -> x.getEnd().isBefore(LocalDateTime.now()))
                .findFirst();

        BookingForItem lastBookingForItem = new BookingForItem();

        if (lastBooking.isPresent()) {
            lastBookingForItem.setId(lastBooking.get().getId());
            lastBookingForItem.setBookerId(lastBooking.get().getBookerId());
            itemDtoForGetItem.setLastBooking(lastBookingForItem);
        }

        bookings = bookingService.findBookingsByItemId(itemDtoForGetItem.getId());

        Optional<Booking> nextBooking = bookings.stream().sorted(startComparator)
                .filter(x -> x.getStart().isAfter(LocalDateTime.now()))
                .findFirst();

        BookingForItem nextBookingForItem = new BookingForItem();

        if (nextBooking.isPresent()) {
            nextBookingForItem.setId(nextBooking.get().getId());
            nextBookingForItem.setBookerId(nextBooking.get().getBookerId());
            itemDtoForGetItem.setNextBooking(nextBookingForItem);
        }

    }

    public ItemDtoForGetItems getItemById(Integer itemId, Integer userId) {

        Item item = itemService.getItemById(itemId);

        ItemDtoForGetItems itemDtoForGetItems = itemMapper.fromDtoToFindAll(item);

        itemDtoForGetItems.setComments(itemService.getCommentsByItemId(itemId).stream()
                .map(commentMapper::toDto)
                .peek(x -> x.setAuthorName(userService.getUserById(x.getAuthorId()).getName()))
                .collect(Collectors.toList()));

        if (!item.getOwnerId().equals(userId)) {
            return itemDtoForGetItems;
        }

        setLastAndNextBooking(itemDtoForGetItems);

        return itemDtoForGetItems;

    }

    public BookingDto addBooking(Booking booking, Integer bookerId) {

        if (!userService.isExists(bookerId)) {
            log.error("CommonService.addBooking: пользователя с id={} в базе нет", bookerId);
            throw new UserNotFoundException("Такого пользователя в базе нет");
        }

        if (!itemService.isExist(booking.getItemId())) {
            log.error("CommonService.addBooking: вещи с id={} в базе нет", booking.getItemId());
            throw new ItemNotFoundException("Такой вещи в базе нет");
        }

        if (!itemService.getItemById(booking.getItemId()).getAvailable()) {
            log.error("CommonService.addBooking: вещь с id={} недоступна для бронирования", booking.getItemId());
            throw new IncorrectItemStatusForBookingException("Вещь недопустна для бронирования");
        }

        if (itemService.getItemById(booking.getItemId()).getOwnerId().equals(bookerId)) {
            log.error("CommonService.addBooking: Ах ты хитрый жук, хочешь забронить свою вещь?");
            throw new BookingNotFoundException("Бронирование нельзя создавать владельцем этой вещи");
        }

        BookingDto bookingDto = bookingService.addBooking(booking, bookerId);

        bookingDto.getItem().setName(itemService.getItemById(bookingDto.getItem().getId()).getName());

        return bookingDto;

    }

    public BookingDto approveBooking(Integer ownerId, Integer bookingId, Boolean approved) {

        BookingDto bookingDto = bookingService.getBookingById(bookingId);

        if (!itemService.getItemById(bookingDto.getItem().getId()).getOwnerId().equals(ownerId)) {
            log.error("CommonService.approveBooking: Подтвердить или отклонить бронирование может только владелец,"
            + "пользователь с id={} не владеет вещью из бронирования с id={}", ownerId, bookingId);
            throw new NoAccessRightsException("Нет прав доступа к бронированию");
        }

        if (!bookingDto.getStatus().equals(BookingStatus.WAITING)) {
            log.error("CommonService.approveBooking: Статус бронирования {} с id={} уже был изменён "
                    + "владельцем вещи с id={}", bookingDto.getStatus(), bookingId, ownerId);
            throw new RepeatRequestException("Запрос уже был обработан владельцем вещи");
        }

        if (approved) {
            bookingDto.setStatus(BookingStatus.APPROVED);
        } else {
            bookingDto.setStatus(BookingStatus.REJECTED);
        }

        bookingDto = bookingService.updateBookingStatus(bookingDto);

        bookingDto.getItem().setName(itemService.getItemById(bookingDto.getItem().getId()).getName());

        return bookingDto;

    }

    public BookingDto getBookingById(Integer bookerId, Integer bookingId) {

        BookingDto bookingDto = bookingService.getBookingById(bookingId);

        if (!bookingDto.getBooker().getId().equals(bookerId)
                && !itemService.getItemById(bookingDto.getItem().getId()).getOwnerId().equals(bookerId)) {
            log.error("CommonService.getBookingById: Бронирование может получить только его создатель "
                    + "или владелец вещи для бронирования");
            throw new NoAccessRightsException("Нет прав доступа к бронированию");
        }

        bookingDto.getItem().setName(itemService.getItemById(bookingDto.getItem().getId()).getName());

        return bookingDto;

    }

    public List<BookingDto> getAllBookingsByBookerIdDesc(Integer bookerId, String state) {

        if (!userService.isExists(bookerId)) {
            log.error("CommonService.getAllBookingsByBookerIdDesc: пользователя с id={} в базе нет", bookerId);
            throw new UserNotFoundException("Такого пользователя в базе нет");
        }

        List<BookingDto> bookingsByBookerId = bookingService.findBookingsByBookerId(bookerId);

        return getBookingDtos(state, bookingsByBookerId);
    }

    public List<BookingDto> getAllBookingsByItemOwnerId(Integer ownerId, String state) {

        if (!userService.isExists(ownerId)) {
            log.error("CommonService.getAllBookingsByItemOwnerId: пользователя с id={} в базе нет", ownerId);
            throw new UserNotFoundException("Такого пользователя в базе нет");
        }

        List<Item> itemsByOwnerId = itemService.getItemsListByOwnerId(ownerId);

        if (itemsByOwnerId.size() == 0) {
            return new ArrayList<>();
        }

        List<Integer> itemsIdByOwnerId = itemsByOwnerId.stream()
                .map(Item::getId)
                .collect(Collectors.toList());

        List<BookingDto> bookingsByItemOwnerId = bookingService.findBookingsByIdItemsList(itemsIdByOwnerId);

        return getBookingDtos(state, bookingsByItemOwnerId);

    }

    private List<BookingDto> getBookingDtos(String state, List<BookingDto> bookingsByItemOwnerId) {

        Comparator<BookingDto> comparator = (o1, o2) -> {
            if (o1.getStart().isBefore(o2.getStart())) {
                return 1;
            } else if (o1.getStart().isAfter(o2.getStart())) {
                return -1;
            } else {
                return 0;
            }
        };

        switch (state) {

            case "CURRENT":
                return bookingsByItemOwnerId.stream()
                        .filter(x -> (x.getStart().isBefore(LocalDateTime.now()) && x.getEnd().isAfter(LocalDateTime.now())))
                        .peek(x -> x.getItem().setName(itemService.getItemById(x.getItem().getId()).getName()))
                        .sorted(comparator)
                        .collect(Collectors.toList());

            case "PAST":
                return bookingsByItemOwnerId.stream()
                        .filter(x -> x.getEnd().isBefore(LocalDateTime.now()))
                        .peek(x -> x.getItem().setName(itemService.getItemById(x.getItem().getId()).getName()))
                        .sorted(comparator)
                        .collect(Collectors.toList());

            case "FUTURE":
                return bookingsByItemOwnerId.stream()
                        .filter(x -> x.getStart().isAfter(LocalDateTime.now()))
                        .peek(x -> x.getItem().setName(itemService.getItemById(x.getItem().getId()).getName()))
                        .sorted(comparator)
                        .collect(Collectors.toList());

            case "WAITING":
                return bookingsByItemOwnerId.stream()
                        .filter(x -> x.getStatus().equals(BookingStatus.WAITING))
                        .peek(x -> x.getItem().setName(itemService.getItemById(x.getItem().getId()).getName()))
                        .sorted(comparator)
                        .collect(Collectors.toList());

            case "REJECTED":
                return bookingsByItemOwnerId.stream()
                        .filter(x -> x.getStatus().equals(BookingStatus.REJECTED))
                        .peek(x -> x.getItem().setName(itemService.getItemById(x.getItem().getId()).getName()))
                        .sorted(comparator)
                        .collect(Collectors.toList());

            case "ALL":
                return bookingsByItemOwnerId.stream()
                        .peek(x -> x.getItem().setName(itemService.getItemById(x.getItem().getId()).getName()))
                        .sorted(comparator)
                        .collect(Collectors.toList());

            default:
                log.error("CommonService.getAllBookingsByBookerIdDesc: Неверный параметр state={}", state);
                throw new IncorrectStateException("Unknown state: " + state);

        }
    }

    public CommentDto addComment(Comment comment, Integer itemId, Integer authorId) {

        if (!userService.isExists(authorId)) {
            log.error("CommonService.addComment: пользователя с id={} в базе нет", authorId);
            throw new UserNotFoundException("Такого пользователя в базе нет");
        }

        if (!itemService.isExist(itemId)) {
            log.error("CommonService.addComment: вещи с id={} в базе нет", itemId);
            throw new ItemNotFoundException("Такой вещи в базе нет");
        }

        Optional<Booking> booking = bookingService.findBookingsByItemId(itemId).stream()
                .filter(x -> x.getEnd().isBefore(LocalDateTime.now()))
                .findFirst();

        if (booking.isEmpty()) {
            log.error("CommonService.addComment: бронирований для вещи с id={} не было", itemId);
            throw new BookingForCommentNotFoundException("Бронирования для запрошенной вещи не было");
        }

        if (!booking.get().getBookerId().equals(authorId)) {
            log.error("CommonService.addComment: пользователь с id={} не брал вещь с id={} в аренду", authorId, itemId);
            throw new NoAccessRightsException("Пользователь не брал вещь в аренду");
        }

        comment = itemService.addComment(comment, itemId, authorId);

        CommentDto commentDto = commentMapper.toDto(comment);

        commentDto.setAuthorName(userService.getUserById(comment.getAuthorId()).getName());

        return commentDto;



    }
}
