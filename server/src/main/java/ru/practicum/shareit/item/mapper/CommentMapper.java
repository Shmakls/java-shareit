package ru.practicum.shareit.item.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;

import java.util.Optional;

@Component
public class CommentMapper {

    public CommentDto toDto(Comment comment) {

        CommentDto commentDto = new CommentDto();

        commentDto.setCreated(comment.getCreated());
        commentDto.setText(comment.getText());
        commentDto.setAuthorId(comment.getAuthorId());

        Optional.ofNullable(comment.getId()).ifPresent(commentDto::setId);

        return commentDto;

    }

}
