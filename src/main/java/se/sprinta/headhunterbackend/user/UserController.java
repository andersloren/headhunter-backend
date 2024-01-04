package se.sprinta.headhunterbackend.user;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import se.sprinta.headhunterbackend.system.Result;
import se.sprinta.headhunterbackend.system.StatusCode;
import se.sprinta.headhunterbackend.user.converter.UserToUserDtoConverter;
import se.sprinta.headhunterbackend.user.dto.UserDto;

import java.util.List;

@RestController
@RequestMapping("${api.endpoint.base-url}/users")
public class UserController {

    private final UserService userService;
    private final UserToUserDtoConverter userToUserDtoConverter;

    public UserController(UserService userService, UserToUserDtoConverter userToUserDtoConverter) {
        this.userService = userService;
        this.userToUserDtoConverter = userToUserDtoConverter;
    }

    @GetMapping
    public Result findAllUsers() {
        List<User> foundUsers = this.userService.findAll();
        List<UserDto> foundUserDtos = foundUsers.stream()
                .map(this.userToUserDtoConverter::convert)
                .toList();
        return new Result(true, StatusCode.SUCCESS, "Find All Success", foundUserDtos);
    }

    @PostMapping
    public Result addUser(@Valid @RequestBody User user) {
        User addedUser = this.userService.save(user);
        UserDto addedUserDto = this.userToUserDtoConverter.convert(addedUser);
        return new Result(true, StatusCode.SUCCESS, "Add Success", addedUserDto);
    }

}
