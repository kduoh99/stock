package com.study.stock.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.study.stock.domain.Stock;
import com.study.stock.repository.StockRepository;

@Service
public class OptimisticLockStockService {

	private final StockRepository stockRepository;

	public OptimisticLockStockService(StockRepository stockRepository) {
		this.stockRepository = stockRepository;
	}

	@Transactional
	public void decrease(Long id, Long quantity) {
		Stock stock = stockRepository.findByIdWithOptimisticLock(id);

		stock.decrease(quantity);

		stockRepository.save(stock);
	}
}
