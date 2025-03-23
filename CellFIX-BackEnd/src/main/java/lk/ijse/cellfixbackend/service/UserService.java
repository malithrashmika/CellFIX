package lk.ijse.cellfixbackend.service;

import lk.ijse.cellfixbackend.dto.ResponseDTO;
import lk.ijse.cellfixbackend.dto.UserDTO;

public interface UserService {
    int saveUser(UserDTO userDTO);
    UserDTO searchUser(String username);
}
