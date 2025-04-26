package com.study.stock.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.study.stock.domain.Stock;
import com.study.stock.repository.StockRepository;

@SpringBootTest
public class StockServiceTest {

	@Autowired
	private StockService stockService;

	@Autowired
	private StockRepository stockRepository;

	@BeforeEach
	public void before() {
		stockRepository.saveAndFlush(new Stock(1L, 100L));
	}

	@AfterEach
	public void after() {
		stockRepository.deleteAll();
	}

	@Test
	public void 재고감소() {
		stockService.decrease(1L, 1L);

		// 100 - 1 = 99
		Stock stock = stockRepository.findById(1L).orElseThrow();

		assertEquals(99, stock.getQuantity());
	}

	// Race Condition
	@Test
	public void 동시에_100개_요청() throws InterruptedException {
		int threadCount = 100;
		ExecutorService executorService = Executors.newFixedThreadPool(32);
		CountDownLatch latch = new CountDownLatch(threadCount);

		for (int i = 0; i < threadCount; i++) {
			executorService.submit(() -> {
				try {
					stockService.decrease(1L, 1L);
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await();

		// 100 - (1 * 100) = 0
		Stock stock = stockRepository.findById(1L).orElseThrow();

		assertEquals(0, stock.getQuantity());
	}
}
