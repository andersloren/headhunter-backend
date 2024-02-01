package se.sprinta.headhunterbackend.user;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import se.sprinta.headhunterbackend.system.Result;
import se.sprinta.headhunterbackend.system.StatusCode;
import se.sprinta.headhunterbackend.user.converter.UserToUserDtoConverter;
import se.sprinta.headhunterbackend.user.dto.UserDto;

import java.util.List;

@RestController
@RequestMapping("${api.endpoint.base-url-users}")
@CrossOrigin("http://localhost:3000")
public class UserController {

    private final UserService userService;
    private final UserToUserDtoConverter userToUserDtoConverter;

    public UserController(UserService userService, UserToUserDtoConverter userToUserDtoConverter) {
        this.userService = userService;
        this.userToUserDtoConverter = userToUserDtoConverter;
    }

    @GetMapping("/findAll")
    public Result findAllUsers() {
        List<User> foundUsers = this.userService.findAll();
        List<UserDto> foundUserDtos = foundUsers.stream()
                .map(this.userToUserDtoConverter::convert)
                .toList();
        return new Result(true, StatusCode.SUCCESS, "Find All Success", foundUserDtos);
    }

    @GetMapping("/findUser/{email}")
    public Result findUserByEmail(@PathVariable String email) {
        User foundUser = this.userService.findByUserEmail(email);
        UserDto foundUserDto = this.userToUserDtoConverter.convert(foundUser);
        return new Result(true, StatusCode.SUCCESS, "Find One Success", foundUserDto);
    }

    @PostMapping("/register")
    public Result registerUser(@Valid @RequestBody User user) {
        User addedUser = this.userService.save(user);
        UserDto addedUserDto = this.userToUserDtoConverter.convert(addedUser);
        return new Result(true, StatusCode.SUCCESS, "Add Success", addedUserDto);
    }

    @PostMapping("/addUser")
    public Result addUser(@Valid @RequestBody User user) {
        User addedUser = this.userService.save(user);
        UserDto addedUserDto = this.userToUserDtoConverter.convert(addedUser);
        return new Result(true, StatusCode.SUCCESS, "Add Success", addedUserDto);
    }

    @PutMapping("/update/{email}") // TODO: 31/01/2024 add username
    public Result updateUser(@PathVariable String email, @RequestBody String roles) {
        User user = this.userService.update(email, roles);
        UserDto updatedUserDto = this.userToUserDtoConverter.convert(user);
        return new Result(true, StatusCode.SUCCESS, "Update Success", updatedUserDto);
    }

    @DeleteMapping("/delete/{email}")
    public Result deleteUser(@PathVariable String email) {
        this.userService.delete(email);
        return new Result(true, StatusCode.SUCCESS, "Delete Success");
    }
}
