package com.study.stock.facade;

import org.springframework.stereotype.Component;

import com.study.stock.service.OptimisticLockStockService;

@Component
public class OptimisticLockStockFacade {

	private final OptimisticLockStockService optimisticLockStockService;

	public OptimisticLockStockFacade(OptimisticLockStockService optimisticLockStockService) {
		this.optimisticLockStockService = optimisticLockStockService;
	}

	public void decrease(Long id, Long quantity) throws InterruptedException {
		while (true) {
			try {
				optimisticLockStockService.decrease(id, quantity);

				break;
			} catch (Exception e) {
				Thread.sleep(50);
			}
		}
	}
}
