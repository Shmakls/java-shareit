package ru.practicum.shareit.requests.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.requests.model.ItemRequest;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Integer> {

    List<ItemRequest> findItemRequestsByRequestorIdOrderByCreatedDesc(Integer requestorId);

    Page<ItemRequest> findAllByRequestorIdNot(Integer requestorId, Pageable pageable);

}
