package com.cafe.cafe_management.DAO;

import com.cafe.cafe_management.Model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillDAO extends JpaRepository<Bill, Integer> {

    List<Bill> getAllBills();

    List<Bill> getBillByUsername(@Param("username") String username);
}
