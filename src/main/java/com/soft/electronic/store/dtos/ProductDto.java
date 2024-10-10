package com.soft.electronic.store.dtos;

import com.soft.electronic.store.entities.Category;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {

    private String productId;
    @NotBlank(message = "Title is required")
    @Size(min = 4,message = "Title must be of minimum 4 chars")
    private String title;
    @NotBlank(message = "Description is required")
    @Size(min = 10,message = "Description must be of minimum 10 characters")
    private String description;
    @NotBlank(message = "Price is required")
    private int price;
    @NotBlank(message = "Discount Price is required")
    private int discountedPrice;
    @NotBlank(message = "Quantity is required")
    private int quantity;
    @NotBlank(message = "Added Date is required")
    private Date addedDate;
    private boolean live;
    private boolean stock;
    @NotBlank(message = "Specification is required")
    @Size(min = 10,message = "Specification must be of minimum 10 characters")
    private String specification;
    @NotBlank(message = "Size is required")
    private int size;
    private String productImageName;
    private CategoryDto category;
}
