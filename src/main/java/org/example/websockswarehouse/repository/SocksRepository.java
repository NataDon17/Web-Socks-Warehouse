package org.example.websockswarehouse.repository;

import org.example.websockswarehouse.model.Sock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface SocksRepository extends JpaRepository<Sock, Integer> {
    @Transactional
    @Modifying
    @Query(value = "UPDATE socks SET quantity = quantity + :quantity " +
            "WHERE color = :color and cotton_part = :cottonPart",
            nativeQuery = true)
    void updateQuantityWhenSocksArrive(String color, int cottonPart, int quantity);

    @Transactional
    @Modifying
    @Query(value = "UPDATE socks SET quantity = quantity - :quantity " +
            "WHERE color = :color and cotton_part = :cottonPart",
            nativeQuery = true)
    void updateQuantityWhenSocksDisposal(String color, int cottonPart, int quantity);
}

