package com.electronic.store.services;

import com.electronic.store.dtos.PageableResponse;
import com.electronic.store.dtos.ProductDto;

public interface ProductService {

	// create
	ProductDto create(ProductDto productDto);

	// update
	ProductDto update(ProductDto productDto, String productId);

	// delete
	void delete(String productId);

	// get single
	ProductDto getProduct(String productId);

	// get all
	PageableResponse<ProductDto> getAllProducts(int pageNumber, int pageSize, String sortBy, String sortDir);

	// get all live
	PageableResponse<ProductDto> getAllLiveProducts(int pageNumber, int pageSize, String sortBy, String sortDir);

	// search product
	PageableResponse<ProductDto> searchByTitle(String subTitle, int pageNumber, int pageSize, String sortBy,
			String sortDir);

	//create product with category
	ProductDto createProductWithCategory(ProductDto productDto, String categoryId);
	
	//update category of product
	ProductDto updateCategoryToProduct(String productId, String categoryId);
	
	//fetch all products under category
	PageableResponse<ProductDto> getProductsOfCategory(String categoryId, int pageNumber, int pageSize, String sortBy, String sortDir);
	
	// other methods
	

}
