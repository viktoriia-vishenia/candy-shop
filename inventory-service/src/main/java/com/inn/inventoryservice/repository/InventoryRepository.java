package com.inn.inventoryservice.repository;

import com.inn.inventoryservice.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface InventoryRepository extends JpaRepository<Inventory, Long> {

//    @Query("SELECT i FROM Inventory i WHERE i.invCode IN :invCode")
    List<Inventory> findByInvCodeIn(@Param("invCodes") List <String> invCode);
}
