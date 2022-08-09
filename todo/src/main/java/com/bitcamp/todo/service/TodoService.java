package com.bitcamp.todo.service;

import com.bitcamp.todo.dto.TodoDTO;
import com.bitcamp.todo.model.TodoEntity;
import com.bitcamp.todo.persistence.TodoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TodoService {

    @Autowired
    private TodoRepository todoRepository;

    public String testService(TodoDTO todoDTO) {
        // TodoEntity 생성
        TodoEntity entity = TodoDTO.toEntity(todoDTO);

        // TodoEntity 저장
        TodoEntity save = todoRepository.save(entity);

        return save.getTitle();
    }

    // 삭제
    public List<TodoEntity> delete(final TodoEntity entity) {
        validate(entity);

        try{
            todoRepository.delete(entity);
        } catch (Exception e) {
            log.error("error deleting entity", entity.getId(), e);
            throw new RuntimeException("error deleting entity" + entity.getId());
        }
        return retrieve(entity.getUserId());
    }

    //수정
    public List<TodoEntity> update(final TodoEntity entity) {
        validate(entity);

        final Optional<TodoEntity> original = todoRepository.findById(entity.getId());

        original.ifPresent(todo -> {
            // 반환된 TodoEntity가 존재하면 값을 새 entity의 값으로 덮어 씌운다.
            todo.setTitle(entity.getTitle());
            todo.setDone(entity.isDone());

            todoRepository.save(todo);
        });
        return retrieve(entity.getUserId());
    }

    // 조회
    public List<TodoEntity> retrieve(final String userId) {
        return todoRepository.findAllByUserId(userId);
    }

    public List<TodoEntity> create(final TodoEntity entity) {
        // 저장할 엔티티 유효성 검사
        validate(entity);

        // 넘겨받은 엔티티를 저장
        todoRepository.save(entity);

        log.info("Entity Id : {} is saved", entity.getUserId());

        return todoRepository.findAllByUserId(entity.getUserId());
    }

    private void validate(final TodoEntity entity) {
        if (entity == null){
            log.warn("Entity cannot be null");
            throw new RuntimeException("Entity cannot be null");
        }

        if (entity.getUserId() == null) {
            log.warn("Unknown user");
            throw new RuntimeException("Unknown user");
        }
    }

}
