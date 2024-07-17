package com.example.backend.model.repository.member;

import com.example.backend.model.entity.member.Refresh;
import jakarta.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshRepository extends CrudRepository<Refresh, String> {

    Boolean existsByRefresh(String refresh);
    Refresh findByRefresh(String refresh);
}
