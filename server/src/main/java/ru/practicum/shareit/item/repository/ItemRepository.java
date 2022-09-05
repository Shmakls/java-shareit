package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {

    @Query(value = " select i from Item i " +
            " where upper(i.name) like upper(concat('%', ?1, '%')) " +
            " or upper(i.description) like upper(concat('%', ?1, '%'))")
    Page<Item> searchItemForRentByText(String text, Pageable pageable);

    List<Item> findItemsByOwnerId(Integer ownerId);

    List<Item> findItemsByRequestId(Integer requestId);

    Page<Item> findItemsBy(Pageable pageable);

    Page<Item> findItemsByOwnerId(Integer ownerId, Pageable pageable);

}
