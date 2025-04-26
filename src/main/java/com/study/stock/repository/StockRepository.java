package com.study.stock.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.study.stock.domain.Stock;

public interface StockRepository extends JpaRepository<Stock, Long> {
}
