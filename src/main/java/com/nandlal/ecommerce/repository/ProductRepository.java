package com.nandlal.ecommerce.repository;

import com.nandlal.ecommerce.model.Category;
import com.nandlal.ecommerce.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Product findByProductName(String productName);

    Page<Product> findByCategoryOrderByPriceAsc(Pageable pageDetails, Category category);

    Page<Product> findByProductNameLikeIgnoreCase(String s, Pageable pageDetails);
}
