package com.shopping.productservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.shopping.productservice.dao.ProductRepository;
import com.shopping.productservice.entity.Product;
import com.shopping.productservice.service.ProductService;

@SpringBootTest
class ProductServiceApplicationTests {

	@Test
	void contextLoads() {
	}
	@InjectMocks
	private ProductService service;
	
	@Mock
	private ProductRepository repository;
	
	@Test
	public void Test_addProduct() { 
		Product product = new Product("a1", 1, "OnePlus", null, "OnePlus", "digital Watch", "Watch",22,2929.99,10);
		
		when(repository.save(product)).thenReturn(product);
		assertEquals(product, service.addProduct(product));  
		
	}

}
