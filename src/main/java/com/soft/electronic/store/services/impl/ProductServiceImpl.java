package com.soft.electronic.store.services.impl;

import com.soft.electronic.store.dtos.PageableResponse;
import com.soft.electronic.store.dtos.ProductDto;
import com.soft.electronic.store.entities.Category;
import com.soft.electronic.store.entities.Product;
import com.soft.electronic.store.exceptions.ResourceNotFound;
import com.soft.electronic.store.helper.Helper;
import com.soft.electronic.store.repositories.CategoryRepository;
import com.soft.electronic.store.repositories.ProductRepository;
import com.soft.electronic.store.services.ProductService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper mapper;
    private Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
    @Value("${product.profile.image.path}")
    private String imagePath;

    @Override
    public ProductDto create(ProductDto productDto) {
        Product product = mapper.map(productDto, Product.class);
        //productID
        String productId = UUID.randomUUID().toString();
        product.setProductId(productId);
        //product added date
        product.setAddedDate(new Date());
        Product savedProduct = productRepository.save(product);
        return mapper.map(savedProduct, ProductDto.class);
    }

    @Override
    public ProductDto update(ProductDto productDto, String productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFound("Product not found with given Product ID!!"));
        product.setTitle(productDto.getTitle());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setDiscountedPrice(productDto.getDiscountedPrice());
        product.setSize(productDto.getSize());
        product.setLive(productDto.isLive());
        product.setStock(productDto.isStock());
        product.setQuantity(productDto.getQuantity());
        product.setSpecification(productDto.getSpecification());
        product.setProductImageName(productDto.getProductImageName());
        Product updatedProduct = productRepository.save(product);


        return mapper.map(updatedProduct, ProductDto.class);

    }

    @Override
    public void delete(String productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFound("Product not found with given Product ID!!"));
        String fullPath = imagePath+product.getProductImageName();

        try{
            Path path = Paths.get(fullPath);
            Files.delete(path);
            logger.info("Deleted image successfully : ",fullPath);
        }catch (NoSuchFileException ex){
            logger.info("User Image not Found in folder: {}",fullPath);
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //delete user by entity
        productRepository.delete(product);
    }

    @Override
    public ProductDto get(String productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFound("Product not found with given Product ID!!"));
        return mapper.map(product, ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir) {

        Sort sort = (sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> page = productRepository.findAll(pageable);
        return Helper.getPageableResponse(page, ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getAllLive(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> page = productRepository.findByLiveTrue(pageable);
        return Helper.getPageableResponse(page, ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> searchByTitle(String subTitle,int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> page = productRepository.findByTitleContaining(subTitle,pageable);
        return Helper.getPageableResponse(page, ProductDto.class);
    }

    @Override
    public ProductDto createWithCategory(ProductDto productDto, String categoryId) {
        //fetch the category from dto
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFound("Category not found with this ID"));
        Product product = mapper.map(productDto, Product.class);
        //productID
        String productId = UUID.randomUUID().toString();
        product.setProductId(productId);
        //product added date
        product.setAddedDate(new Date());
        product.setCategory(category);
        Product savedProduct = productRepository.save(product);
        return mapper.map(savedProduct, ProductDto.class);
    }
    @Override
    public ProductDto assignWithCategory(String productId, String categoryId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFound("Product not found with given Product ID!!"));
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFound("Category not found with this ID"));
        product.setCategory(category);
        Product updatedProduct = productRepository.save(product);
        return mapper.map(updatedProduct, ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getAllOfCategory(String categoryId, int pageNumber, int pageSize, String sortBy, String sortDir) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFound("Category not found with this ID"));
        Sort sort = (sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> page = productRepository.findByCategory(category, pageable);
        return Helper.getPageableResponse(page,ProductDto.class);
    }

}
