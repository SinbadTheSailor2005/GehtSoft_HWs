package org.GehtSoftHWByAziz.HW7;

import org.GehtSoftHWByAziz.HW7.RESTAnnotations.*;
import org.GehtSoftHWByAziz.HW7.dto.User;

import java.util.List;

@CustomRestController
@CustomRequestMapping("/api/v1/users")
public class UserController {
  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @CustomGetMapping
  public List<User> getAllUsers() {
    return userService.getAllUsers();
  }

  @CustomGetMapping("/{id}")
  public User getUserById(@CustomPathVariable("id") Long id) {
    return userService.getUserById(id);
  }

  @CustomPostMapping
  public User createUser(@CustomRequestBody User user) {
    return userService.createUser(user);
  }

  @CustomPutMapping("/{id}")
  public User updateUser(@CustomPathVariable("id") Long id,
                            @CustomRequestBody User user) {
    return userService.updateUser(id, user);
  }




  @CustomDeleteMapping("/{id}")
  public void deleteUser(@CustomPathVariable("id") Long id) {
    userService.deleteUser(id);
  }
}

