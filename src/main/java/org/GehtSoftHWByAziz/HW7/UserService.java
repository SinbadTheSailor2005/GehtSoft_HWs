package org.GehtSoftHWByAziz.HW7;

import org.GehtSoftHWByAziz.HW7.dto.User;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserService {

  private Map<Long, User> users = new ConcurrentHashMap<>(); // In-memory storage
  public List<User> getAllUsers() {
    return users.values().stream().toList();

  }
  public User getUserById(Long id) {
    return users.get(id);

  }
  public User createUser(User dto) {
     users.put(dto.id(), dto);
     return dto;


  }
  public User updateUser(Long id, User dto) {
     users.put(id, dto);
     return users.get(id);

  }



  public void deleteUser(Long id) {
    users.remove(id);
  }

}
