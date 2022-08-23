package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.practicum.shareit.item.dto.ItemDtoForGetItems;
import ru.practicum.shareit.item.exceptions.InvalidCommentTextException;
import ru.practicum.shareit.item.exceptions.ItemNotFoundException;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.validators.ItemValidator;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exceptions.IncorrectItemOwnerId;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDtoForItemRequestDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    private final CommentRepository commentRepository;

    private final ItemMapper itemMapper;

    private final ItemValidator itemValidator;

    @Override
    public ItemDto addItem(Integer userId, ItemDto itemDto) {

        Item item = itemMapper.fromDto(itemDto);

        item.setOwnerId(userId);

        itemValidator.isValid(item);

        item = itemRepository.save(item);

        return itemMapper.toDto(item);

    }

    @Override
    public ItemDto updateItem(Integer userId, ItemDto itemDto, Integer itemId) {

        Item updateItem = itemMapper.fromDto(itemDto);

        Item oldItem = itemRepository.getReferenceById(itemId);

        if (!oldItem.getOwnerId().equals(userId)) {
            log.error("ItemService: вещь с id={} не принадлежит пользователю с id={}", itemId, userId);
            throw new IncorrectItemOwnerId("Нельзя редактировать не принадлежащую вам вещь");
        }

        log.info("ItemService: вещь с id={} обновлена", itemId);

        Item item = itemConstructorToUpdate(updateItem, oldItem);

        item.setId(itemId);

        item = itemRepository.save(item);

        return itemMapper.toDto(item);

    }

    @Override
    public Item getItemById(Integer itemId) {

        if (!itemRepository.existsById(itemId)) {
            log.error("ItemService: Вещи с id={} в базе нет", itemId);
            throw new ItemNotFoundException("Такой вещи в базе нет");
        }

        return itemRepository.getReferenceById(itemId);

    }

    @Override
    public List<Item> getItemsListByOwnerId(Integer userId, Integer from, Integer size) {

        int page = from / size;

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        Page<Item> items = itemRepository.findItemsByOwnerId(userId, pageable);

        log.info("ItemService: направляю запрос в ItemDb для получения списка вещей владельца с id={} ", userId);

        return items.getContent();

    }

    @Override
    public List<Item> getItemsListByOwnerId(Integer userId) {

        log.info("ItemService: направляю запрос в ItemDb для получения списка вещей владельца с id={} ", userId);

        return itemRepository.findItemsByOwnerId(userId);
    }

    @Override
    public List<ItemDto> searchItemForRentByText(String text, Integer from, Integer size) {

        if (!StringUtils.hasText(text)) {
            log.info("ItemService: текст для поиска пустой, возвращаю пустой список");
            return new ArrayList<>();
        } else {
            log.info("ItemService: направляю запрос в ItemDb для поиска вещей содержащих текст \"{}\"", text);
            Pageable pageable = PageRequest.of(from / size, size, Sort.by("id").ascending());
            return itemRepository.searchItemForRentByText(text, pageable).stream()
                    .filter(x -> x.getAvailable().equals(true))
                    .map(itemMapper::toDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public List<ItemDtoForGetItems> findAllItems(Integer from, Integer size) {

        int page = from / size;

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        Page<Item> items = itemRepository.findItemsBy(pageable);

        return items.map(itemMapper::fromDtoToFindAll).getContent();

    }

    @Override
    public Boolean isExist(Integer itemId) {
        return itemRepository.existsById(itemId);
    }

    @Override
    public Comment addComment(Comment comment, Integer itemId, Integer authorId) {

        if (!StringUtils.hasText(comment.getText())) {
            log.error("ItemService.addComment: текст комментария пустой или равен null");
            throw new InvalidCommentTextException("Текст коммента пустой или равено null");
        }

        comment.setItemId(itemId);
        comment.setAuthorId(authorId);

        return commentRepository.save(comment);

    }

    @Override
    public List<Comment> getCommentsByItemId(Integer itemId) {
        return commentRepository.findCommentsByItemId(itemId);
    }

    @Override
    public List<ItemDtoForItemRequestDto> getItemsDtoForItemRequestDtoByRequestId(Integer requestId) {

        return itemRepository.findItemsByRequestId(requestId).stream()
                .map(itemMapper::toItemDtoForItemRequestDto)
                .collect(Collectors.toList());

    }

    private Item itemConstructorToUpdate(Item updateItem, Item oldItem) {

        Item item = new Item();

        if (updateItem.getName() != null) {
            item.setName(updateItem.getName());
        } else {
            item.setName(oldItem.getName());
        }

        if (updateItem.getDescription() != null) {
            item.setDescription(updateItem.getDescription());
        } else {
            item.setDescription(oldItem.getDescription());
        }

        if (updateItem.getAvailable() != null) {
            item.setAvailable(updateItem.getAvailable());
        } else {
            item.setAvailable(oldItem.getAvailable());
        }

        if (updateItem.getOwnerId() != null) {
            item.setOwnerId(updateItem.getOwnerId());
        } else {
            item.setOwnerId(oldItem.getOwnerId());
        }

        return item;

    }



}
