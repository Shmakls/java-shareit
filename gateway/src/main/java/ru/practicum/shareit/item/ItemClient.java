package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.ItemGatewayDto;
import ru.practicum.shareit.user.dto.CommentGatewayDto;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {

    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build());
    }

    public ResponseEntity<Object> addItem(ItemGatewayDto itemGatewayDto, Long userId) {
        return post("", userId, itemGatewayDto);
    }

    public ResponseEntity<Object> updateItem(ItemGatewayDto itemGatewayDto, Long itemId, Long userId) {
        return patch("/" + itemId, userId, itemGatewayDto);
    }

    public ResponseEntity<Object> getItemById(Long itemId, Long userId) {
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> getItemsListByOwnerId(Long userId, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
            "from", from,
            "size", size
        );
        return get("?from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> searchItemForRentByText(Long userId, String text, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
            "text", text,
            "from", from,
            "size", size
        );
        return get("/search?text={text}&from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> addComment(Long userId, Integer itemId, CommentGatewayDto commentGatewayDto) {
        return post("/" + itemId + "/comment", userId, commentGatewayDto);
    }

}
