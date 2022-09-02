package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exceptions.IncorrectItemOwnerId;
import ru.practicum.shareit.item.exceptions.InvalidCommentTextException;
import ru.practicum.shareit.item.exceptions.ItemNotFoundException;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemServiceImplTest {

    private final ItemService itemService;

    private final UserService userService;

    private static ItemDto itemDto1;

    private static ItemDto itemDto2;

    private static UserDto userDto1;

    @BeforeAll
    static void beforeAll() {

        itemDto1 = new ItemDto();
        itemDto1.setAvailable(true);
        itemDto1.setDescription("item1Description");
        itemDto1.setName("item1");

        itemDto2 = new ItemDto();
        itemDto2.setAvailable(true);
        itemDto2.setDescription("item2Description");
        itemDto2.setName("item2");

        userDto1 = new UserDto();
        userDto1.setEmail("user1@email");
        userDto1.setName("user1");

    }

    @Test
    void updateItem() {

        userService.addUser(userDto1);

        itemService.addItem(1, itemDto1);

        ItemDto itemDtoToUpdateName = new ItemDto();
        itemDtoToUpdateName.setName("updateItem1");

        final IncorrectItemOwnerId incorrectItemOwnerId = assertThrows(
                IncorrectItemOwnerId.class,
                () -> itemService.updateItem(99, itemDtoToUpdateName, 1)
        );

        assertEquals("Нельзя редактировать не принадлежащую вам вещь", incorrectItemOwnerId.getMessage());

        ItemDto updatedItem = itemService.updateItem(1, itemDtoToUpdateName, 1);

        assertEquals("updateItem1", updatedItem.getName());

        ItemDto itemDtoToUpdateDescription = new ItemDto();
        itemDtoToUpdateDescription.setDescription("updateItem1Description");

        updatedItem = itemService.updateItem(1, itemDtoToUpdateDescription, 1);

        assertEquals("updateItem1Description", updatedItem.getDescription());

        ItemDto itemDtoToUpdateAvailable = new ItemDto();
        itemDtoToUpdateAvailable.setAvailable(false);

        updatedItem = itemService.updateItem(1, itemDtoToUpdateAvailable, 1);

        assertFalse(updatedItem.getAvailable());

        ItemDto itemDtoToUpdateOwnerId = new ItemDto();
        itemDtoToUpdateOwnerId.setOwner(2);

        UserDto userDto2 = new UserDto();
        userDto2.setName("user2");
        userDto2.setEmail("user2@email");

        userService.addUser(userDto2);

        updatedItem = itemService.updateItem(1, itemDtoToUpdateOwnerId, 1);

        assertEquals(2, updatedItem.getOwner());

    }

    @Test
    void getItemById() {

        userService.addUser(userDto1);

        itemService.addItem(1, itemDto1);

        final ItemNotFoundException itemNotFoundException = assertThrows(
                ItemNotFoundException.class,
                () -> itemService.getItemById(99)
        );

        assertEquals("Такой вещи в базе нет", itemNotFoundException.getMessage());

        Item resultItem = itemService.getItemById(1);

        assertEquals(1, resultItem.getId());

    }

    @Test
    void searchItemForRentByText() {

        userService.addUser(userDto1);

        itemService.addItem(1, itemDto1);
        itemService.addItem(1, itemDto2);

        List<ItemDto> emptyResultList = itemService.searchItemForRentByText("", 0, 20);

        assertEquals(0, emptyResultList.size());

        emptyResultList = itemService.searchItemForRentByText(null, 0, 20);

        assertEquals(0, emptyResultList.size());

        List<ItemDto> items = itemService.searchItemForRentByText("1", 0, 20);

        assertEquals(1, items.size());

        items = itemService.searchItemForRentByText("2", 0, 20);

        assertEquals(2, items.get(0).getId());

        ItemDto itemDtoToUpdateStatusItem1 = new ItemDto();
        itemDtoToUpdateStatusItem1.setAvailable(false);

        ItemDto updatedItem = itemService.updateItem(1, itemDtoToUpdateStatusItem1, 1);

        assertFalse(updatedItem.getAvailable());

        items = itemService.searchItemForRentByText("1", 0, 20);

        assertEquals(0, items.size());

    }

    @Test
    void addComment() {

        userService.addUser(userDto1);

        itemService.addItem(1, itemDto1);

        Comment commentWithEmptyText = new Comment();
        commentWithEmptyText.setText("");

        Comment commentWithNullText = new Comment();

        final InvalidCommentTextException e1 = assertThrows(
                InvalidCommentTextException.class,
                () -> itemService.addComment(commentWithEmptyText, 1, 1)
        );

        assertEquals("Текст коммента пустой или равено null", e1.getMessage());

        final InvalidCommentTextException e2 = assertThrows(
                InvalidCommentTextException.class,
                () -> itemService.addComment(commentWithNullText, 1, 1)
        );

        assertEquals("Текст коммента пустой или равено null", e2.getMessage());

        Comment comment = new Comment();
        comment.setText("textComment1");

        comment = itemService.addComment(comment, 1, 1);

        assertEquals(1, comment.getId());

    }
}