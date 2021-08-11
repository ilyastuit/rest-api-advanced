package com.epam.esm.repository.user;

import com.epam.esm.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Integer> {

    public Page<User> findAll(Pageable pageable);

    public List<User> findById(int userId);
}
