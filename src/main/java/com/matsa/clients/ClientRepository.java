package com.matsa.clients;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;
public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByEmailOrPhone(String email, String phone);

    @Query("select client from Client client where lower(client.firstName) like :query " +
            "or lower(client.lastName) like :query " +
            "or lower(client.email) like :query " +
            "or lower(client.phone) like :query ")
    Page<Client> findByQuery(String query, Pageable pageable);
}
