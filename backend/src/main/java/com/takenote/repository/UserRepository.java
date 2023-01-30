package com.takenote.repository;

import com.takenote.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

    String USER_SELECT_FIELDS = """
            SELECT u.id, u.password, u.email, u.name, u.status, u.is_deleted, u.deleted_at, u.created_at, u.updated_at, u.deleted_by, u.created_by, u.updated_by
                """;

    String SELECT_USER_BY_EMAIL = USER_SELECT_FIELDS + """
            FROM user u
            WHERE u.email = ?1
            AND u.is_deleted = FALSE
                """;

    @Query(value = SELECT_USER_BY_EMAIL, nativeQuery = true)
    User findByEmail(String email);
}
