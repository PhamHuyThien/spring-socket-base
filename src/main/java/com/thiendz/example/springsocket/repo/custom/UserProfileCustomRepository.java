package com.thiendz.example.springsocket.repo.custom;

import com.thiendz.example.springsocket.dto.SqlResource;
import com.thiendz.example.springsocket.dto.dto.UserProfileInfoDto;
import com.thiendz.example.springsocket.utils.SqlUtils;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class UserProfileCustomRepository {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    SqlResource sqlResource;

    @SneakyThrows
    public UserProfileInfoDto getUserProfileInfo(String username) {
        String sqlNativeQuery = sqlResource.get("getUserInfoSQL");

        Object[] data = (Object[]) entityManager
                .createNativeQuery(sqlNativeQuery)
                .setParameter(1, username)
                .getSingleResult();

        return SqlUtils.map(sqlNativeQuery, data, UserProfileInfoDto.class);
    }
}
