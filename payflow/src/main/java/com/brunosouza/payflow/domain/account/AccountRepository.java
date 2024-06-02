package com.brunosouza.payflow.domain.account;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Page<Account> findByDueDateAndDescriptionContaining(
            @Param("dueDate") LocalDate dueDate,
            @Param("description") String description,
            Pageable pageable
    );

    Page<Account> findByDescriptionContaining(@Param("description") String description, Pageable pageable);

    Page<Account> findByDueDate(@Param("dueDate") LocalDate dueDate, Pageable pageable);

    Page<Account> findAll(Pageable pageable);

    @Query("SELECT SUM(a.value) FROM Account a WHERE a.paymentDate BETWEEN :startDate AND :endDate")
    BigDecimal findTotalValuePaidByPeriod(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT a FROM Account a WHERE (:dueDate IS NULL OR a.dueDate = :dueDate) AND (:description IS NULL OR lower(a.description) LIKE lower(concat('%', :description, '%')))")
    Page<Account> findByDueDateAndDescription(LocalDate dueDate, String description, Pageable pageable);
}
