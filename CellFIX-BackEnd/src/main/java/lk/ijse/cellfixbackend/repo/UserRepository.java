package lk.ijse.cellfixbackend.repo;

import lk.ijse.cellfixbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,String> {

    User findByEmail(String email);

    boolean existsByEmail(String userName);

    int deleteByEmail(String userName);

}
