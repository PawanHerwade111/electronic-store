package com.electronic.store.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.electronic.store.dtos.ApiResponseMessage;
import com.electronic.store.dtos.CategoryDto;
import com.electronic.store.dtos.PageableResponse;
import com.electronic.store.dtos.ProductDto;
import com.electronic.store.services.CategoryService;
import com.electronic.store.services.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/categories")
public class CatgoryController {

	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private ProductService productService;

	// create
	@PostMapping("/create")
	public ResponseEntity<CategoryDto> createCategoryDto(@Valid @RequestBody CategoryDto categoryDto) {
		CategoryDto categoryDto1 = categoryService.create(categoryDto);
		return new ResponseEntity<>(categoryDto1, HttpStatus.CREATED);

	}

	// update
	@PutMapping("/update/{categoryId}")
	public ResponseEntity<CategoryDto> updateCategoryDto(@RequestBody CategoryDto categoryDto,
			@PathVariable("categoryId") String categoryId) {
		CategoryDto updatedCategory = categoryService.update(categoryDto, categoryId);
		return new ResponseEntity<>(updatedCategory, HttpStatus.OK);

	}

	// delete
	@DeleteMapping("/delete/{categoryId}")
	public ResponseEntity<ApiResponseMessage> deleteCategory(@PathVariable("categoryId") String categoryId) {
		categoryService.delete(categoryId);
		ApiResponseMessage response = ApiResponseMessage.builder().message("Category is deleted Successfully!")
				.status(HttpStatus.OK).success(true).build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	// get all
	@GetMapping("/getAll")
	public ResponseEntity<PageableResponse<CategoryDto>> getAllCategories(
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {
		PageableResponse<CategoryDto> pageableResponse = categoryService.getAllCategories(pageNumber, pageSize, sortBy,
				sortDir);
		return new ResponseEntity<>(pageableResponse, HttpStatus.OK);
	}

	// get Single
	@GetMapping("/{categoryId}")
	public ResponseEntity<CategoryDto> getSingleCategory(@PathVariable("categoryId") String categoryId) {
		CategoryDto categoryDto = categoryService.getCategory(categoryId);
		return ResponseEntity.ok(categoryDto);

	}
	
	// create product with category
	@PostMapping("/{categoryId}/products")
	public ResponseEntity<ProductDto> createProductWithCategory(@PathVariable("categoryId") String categoryId,
			@RequestBody ProductDto productDto) {
		ProductDto productWithCategory = productService.createProductWithCategory(productDto, categoryId);
		return new ResponseEntity<>(productWithCategory, HttpStatus.CREATED);

	}

	// update category for existing product
	@PutMapping("/{categoryId}/{productId}")
	public ResponseEntity<ProductDto> updateCategoryForProduct(@PathVariable("categoryId") String categoryId,
			@PathVariable("productId") String productId) {
		ProductDto productsDto = productService.updateCategoryToProduct(productId, categoryId);
		return new ResponseEntity<>(productsDto, HttpStatus.OK);

	}
	
	// get products of categories
	@GetMapping("/{categoryId}/products")
	public ResponseEntity<PageableResponse<ProductDto>> getProductsOfCategory(
			@PathVariable("categoryId") String categoryId,
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {
		PageableResponse<ProductDto> productsDto = productService.getProductsOfCategory(categoryId, pageNumber, pageSize, sortBy, sortDir);
		return new ResponseEntity<>(productsDto, HttpStatus.OK);

	}
}
