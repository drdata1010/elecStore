package com.soft.electronic.store.services.impl;

import com.soft.electronic.store.dtos.CategoryDto;
import com.soft.electronic.store.dtos.PageableResponse;
import com.soft.electronic.store.dtos.UserDTO;
import com.soft.electronic.store.entities.Category;
import com.soft.electronic.store.entities.User;
import com.soft.electronic.store.exceptions.ResourceNotFound;
import com.soft.electronic.store.helper.Helper;
import com.soft.electronic.store.repositories.CategoryRepository;
import com.soft.electronic.store.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper mapper;
    @Value("${category.profile.image.path}")
    private String imagePath;
    @Override
    public CategoryDto create(CategoryDto categoryDto) {
        //creating categoryId
        String categoryId = UUID.randomUUID().toString();
        categoryDto.setCategoryId(categoryId);
        Category category = mapper.map(categoryDto, Category.class);
        Category savedCategory = categoryRepository.save(category);

        return mapper.map(savedCategory, CategoryDto.class);
    }

    @Override
    public CategoryDto update(CategoryDto categoryDto, String categoryId) {

        //get category if given id
        Category category = categoryRepository.findById(categoryId).orElseThrow(()-> new ResourceNotFound("Category not found with given category ID"));
        category.setCategoryCover(categoryDto.getCategoryCover());
        category.setTitle(categoryDto.getTitle());
        category.setDescription(categoryDto.getDescription());
        Category updatedCategory = categoryRepository.save(category);
        return mapper.map(updatedCategory,CategoryDto.class);
    }

    @Override
    public void delete(String categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(()-> new ResourceNotFound("Category not found with given category ID"));
        categoryRepository.delete(category);
    }

    @Override
    public PageableResponse<CategoryDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir) {
        //Sort class implementation
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<Category> page = categoryRepository.findAll(pageable);
        PageableResponse<CategoryDto> pageableResponse = Helper.getPageableResponse(page,CategoryDto.class);
        return pageableResponse;
    }

    @Override
    public CategoryDto get(String categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(()-> new ResourceNotFound("Category not found with given category ID"));
        return mapper.map(category,CategoryDto.class);
    }

    @Override
    public List<CategoryDto> searchCategory(String keywords) {
        List<Category> categories = categoryRepository.findByTitleContaining(keywords);
        List<CategoryDto> categoryDtos = categories.stream().map(category -> entityToDTO(category)).collect(Collectors.toList());
        return categoryDtos;
    }
    private Category dtoToEntity(CategoryDto categoryDto) {
        //  this is manual method to map dto to entity and convert
//        User user = User.builder()
//                .userId(userDTO.getUserId())
//                .name(userDTO.getName())
//                .email(userDTO.getEmail())
//                .password(userDTO.getPassword())
//                .gender(userDTO.getPassword())
//                .imageName(userDTO.getImageName())
//                .about(userDTO.getAbout()).build();

        //this is easy way to map and convert
        return mapper.map(categoryDto,Category.class);
    }

    private CategoryDto entityToDTO(Category savedCategory) {
//        UserDTO userDto = UserDTO.builder()
//                .userId(savedUser.getUserId())
//                .name(savedUser.getName())
//                .email(savedUser.getEmail())
//                .password(savedUser.getPassword())
//                .gender(savedUser.getGender())
//                .imageName(savedUser.getImageName())
//                .about(savedUser.getAbout())
//                .build();

        return mapper.map(savedCategory, CategoryDto.class);
    }

}
