package com.electronic.store.services;

import com.electronic.store.dtos.CategoryDto;
import com.electronic.store.dtos.PageableResponse;

public interface CategoryService {

	//create
	CategoryDto create(CategoryDto categoryDto);
	
	//update
	CategoryDto update(CategoryDto categoryDto, String categoryId);
	
	//delete
	void delete(String categoryId);
	
	//get all
	PageableResponse<CategoryDto> getAllCategories(int pageNumber, int pageSize, String sortBy, String sortDir);
	
	//get single category detail
	CategoryDto getCategory(String categoryId);
	
	//search if needed
}
