package com.bitcamp.todo.controller;

import com.bitcamp.todo.dto.ResponseDTO;
import com.bitcamp.todo.dto.TodoDTO;
import com.bitcamp.todo.model.TodoEntity;
import com.bitcamp.todo.persistence.TodoRepository;
import com.bitcamp.todo.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/todo")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @Autowired
    private TodoRepository todoRepository;

    @GetMapping("/mytest")
    public ResponseEntity<?> readAll(){
        List<TodoEntity> all = todoRepository.findAll();

        Map result = new HashMap<>();
        result.put("data", all);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/test")
    public String save(@RequestBody TodoDTO todoDTO) {
        return todoService.testService(todoDTO);
    }


    @DeleteMapping
    public ResponseEntity<?> deleteTodo(@RequestBody TodoDTO todoDTO){
        try {
            String temporaryUserId = "temporary-user";

            TodoEntity entity = TodoDTO.toEntity(todoDTO);
            entity.setUserId(temporaryUserId);
            List<TodoEntity> entities = todoService.delete(entity);
            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().responseList(dtos).build();

            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            String error = e.getMessage();
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }

    }

    /**
     * 수정 (Update Todo 구현)
     */
    @PutMapping
    public ResponseEntity<?> updateTodo(@RequestBody TodoDTO todoDTO) {
        String temporaryUserId = "temporary-user";

        TodoEntity entity = TodoDTO.toEntity(todoDTO);
        entity.setUserId(temporaryUserId);

        List<TodoEntity> entities = todoService.update(entity);
        List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

        ResponseDTO<TodoDTO> response =
                ResponseDTO.<TodoDTO>builder().responseList(dtos).build();

        return ResponseEntity.ok().body(response);
    }

    /**
     * 조회(Retrieve Todo 구현)
     */
    @GetMapping
    public ResponseEntity<?> retrieveTodoList() {
        String temporaryUserId = "temporary-user";

        List<TodoEntity> entities = todoService.retrieve(temporaryUserId);
        List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
        ResponseDTO<TodoDTO> response =
                ResponseDTO.<TodoDTO>builder().responseList(dtos).build();

        return ResponseEntity.ok().body(response);
    }

    @PostMapping
    public ResponseEntity<?> createTodo(@RequestBody TodoDTO todoDTO) {
        try {
            String temporaryUserId = "temporary-user";

            // TodoEntity로 변환 (Table로 만듬)
            TodoEntity entity = TodoDTO.toEntity(todoDTO);

            // id를 null로 초기화 한다. 생성 당시에는 id가 없어야 하기 때문
            entity.setId(null);

            // 임시 유저 아이디를 설정해 준다.
            entity.setUserId(temporaryUserId);

            // service 이용, Entity를 생성
            List<TodoEntity> todoEntities = todoService.create(entity);

            // 자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO 리스트로 변환
            List<TodoDTO> dtos =
                    todoEntities.stream().map(TodoDTO::new).collect(Collectors.toList());

            // 변환된 TodoDTO 리스트를 이용해 ResponseDTO를 초기화
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder()
                    .responseList(dtos).build();

            // ResponseDTO를 리턴
            return ResponseEntity.ok().body(response);


        } catch (Exception e) {
            String error = e.getMessage();
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().
                    error(error).build();
            return ResponseEntity.badRequest().body(response);
        }

    }

    /*@PostMapping
    public ResponseEntity<?> createTodo(@RequestBody TodoDTO todoDTO) {
        try {
            String temporaryUserId = "temporary-user";

            // TodoEntity로 변환 (Table로 만듬)
            TodoEntity entity = TodoDTO.toEntity(todoDTO);

            // id를 null로 초기화 한다. 생성 당시에는 id가 없어야 하기 때문
            entity.setId(null);

            // 임시 유저 아이디를 설정해 준다.
            entity.setUserId(temporaryUserId);

            // service 이용, Entity를 생성
            List<TodoEntity> todoEntities = todoService.create(entity);
            // map으로 변환 해서 출력 가능
            // 참고 : https://hyeonic.tistory.com/197
            Map result = new HashMap<>();
            result.put("data", todoEntities);
//            result.put("count", todoEntities.size());

            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch (Exception e) {
            String error = e.getMessage();
            return null;
        }

    }*/

    @PostMapping("/mytest")
    public ResponseEntity<?> createTodoTest(@RequestBody TodoDTO todoDTO) {
        try {
            String temporaryUserId = "temporary-user";

            // TodoEntity로 변환 (Table로 만듬)
            TodoEntity entity = TodoDTO.toEntity(todoDTO);

            // id를 null로 초기화 한다. 생성 당시에는 id가 없어야 하기 때문
            entity.setId(null);

            // 임시 유저 아이디를 설정해 준다.
            entity.setUserId(temporaryUserId);

            // service 이용, Entity를 생성
            List<TodoEntity> todoEntities = todoService.create(entity);
            // map으로 변환 해서 출력 가능
            // 참고 : https://hyeonic.tistory.com/197
            Map result = new HashMap<>();
            result.put("data", todoEntities);
//            result.put("count", todoEntities.size());

            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch (Exception e) {
            String error = e.getMessage();
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

    }

}
