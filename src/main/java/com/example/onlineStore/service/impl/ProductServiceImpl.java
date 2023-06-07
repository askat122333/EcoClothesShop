package com.example.onlineStore.service.impl;

import com.example.onlineStore.dto.MvcDto.ProductMvcDto;
import com.example.onlineStore.dto.ProductDto;
import com.example.onlineStore.dto.SearchDto;
import com.example.onlineStore.entity.Category;
import com.example.onlineStore.entity.Discount;
import com.example.onlineStore.entity.Product;
import com.example.onlineStore.enums.ProductType;
import com.example.onlineStore.exceptions.ProductNotFoundException;
import com.example.onlineStore.exceptions.ValidException;
import com.example.onlineStore.repository.ProductRepository;
import com.example.onlineStore.service.CategoryService;
import com.example.onlineStore.service.DiscountService;
import com.example.onlineStore.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import javax.validation.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    private final EntityManager entityManager;
    private final DiscountService discountService;
    private final CategoryService categoryService;

    private ProductDto mapToDto(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .size(product.getSize())
                .price(product.getPrice())
                .material(product.getMaterial())
                .productType(product.getProductType())
                .dateAdded(product.getDateAdded())
                .discount(product.getDiscount())
                .category(product.getCategory())
                .build();
    }
    public ProductMvcDto mapToDtoWithImage(Product product) {
        return ProductMvcDto.builder()
                .id(product.getId())
                .name(product.getName())
                .image(product.getImage())
                .size(product.getSize())
                .price(product.getPrice())
                .material(product.getMaterial())
                .productType(product.getProductType())
                .dateAdded(product.getDateAdded())
                .build();
    }

    @Override
    public ProductDto getById(Long id) throws ProductNotFoundException {
        try {
            Product product = productRepository.findByIdAndRdtIsNull(id);
            return mapToDto(product);
        }catch (NullPointerException e){
            log.error("Метод getById(Product), Exception: Продукт с id "+id+" не найден.");
            throw new ProductNotFoundException("Продукт с id "+id+" не найден.");
        }


    }

    @Override
    public Product getByIdEntity(Long id) {
        return productRepository.findByIdAndRdtIsNull(id);
    }

    @Override
    public List<ProductDto> getAll() throws ProductNotFoundException {

            List<Product> productList = productRepository.findAllByRdtIsNull();
            if (productList.isEmpty()) {
                log.error("Метод getAll(Product), Exception: В базе нет товаров.");
                throw new ProductNotFoundException("В базе нет товаров.");
            }
            List<ProductDto> productDtoList = new ArrayList<>();
            for (Product product:productList) {
                productDtoList.add(mapToDto(product));
            }
            return productDtoList;

    }
@Override
public ProductDto create(@Valid ProductDto dto) {
    Product product = Product.builder()
            .name(dto.getName())
            .price(dto.getPrice())
            .dateAdded(LocalDate.now())
            .productType(ProductType.NEW)
            .material(dto.getMaterial())
            .size(dto.getSize())
            .build();

    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();
    Set<ConstraintViolation<Product>> violations = validator.validate(product);

    if (!violations.isEmpty()) {

        List<String> errorMessages = new ArrayList<>();
        for (ConstraintViolation<Product> violation : violations) {
            errorMessages.add("Поле: " + violation.getPropertyPath() + " - " + violation.getMessage());
            log.warn("Метод create(Product): "+errorMessages);
        }
        throw new ValidException(errorMessages);
    }

    return mapToDto(productRepository.save(product));
}
    @Override
    public List<ProductDto> getAllByType() throws ProductNotFoundException {
        List<Product> productList = productRepository.findAllByProductTypeAAndRdtIsNull();
        if (productList.isEmpty()){
            log.error("Метод getAllByType(Product), Exception: В базе нет новинок.");
            throw new ProductNotFoundException("В базе нет новинок.");
        }
        List<ProductDto> productDtoList = new ArrayList<>();
        for (Product product:productList) {
            productDtoList.add(mapToDto(product));
        }
        return productDtoList;
    }

    @Override
    public List<ProductMvcDto> getAllMvc() throws ProductNotFoundException {
        List<Product> productList = productRepository.findAllByRdtIsNull();
        if (productList.isEmpty()) {
            log.error("Метод getAll(Product), Exception: В базе нет товаров.");
            throw new ProductNotFoundException("В базе нет товаров.");
        }
        List<ProductMvcDto> productDtoList = new ArrayList<>();
        for (Product product:productList) {
            productDtoList.add(mapToDtoWithImage(product));
        }
        return productDtoList;
    }

    @Override
    public ProductDto addCategory(Long productId,Long categoryId){
        Category category = categoryService.getByIdEntity(categoryId);
        Product product = getByIdEntity(productId);
        product.setCategory(category);
        productRepository.save(product);
        return mapToDto(product);
    }

    @Override
    public ProductDto addDiscount(Long productId,Long discountId) {
        Discount discount = discountService.getByIdEntity(discountId);
        Product product = getByIdEntity(productId);
        product.setDiscount(discount);
        productRepository.save(product);
        return mapToDto(product);
    }

    @Override
    public List<ProductDto> dynamicSearch(SearchDto searchDto) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> query = cb.createQuery(Product.class);
        Root<Product> root = query.from(Product.class);

        List<Predicate> predicates = new ArrayList<>();

        if (searchDto.getMaterial() != null) {
            predicates.add(cb.equal(root.get("material"), searchDto.getMaterial()));
        }

        if (searchDto.getName() != null) {
            predicates.add(cb.equal(root.get("name"), searchDto.getName()));
        }

        if (searchDto.getPrice() != null) {
            predicates.add(cb.equal(root.get("price"), searchDto.getPrice()));
        }

        if (searchDto.getSize() != null) {
            predicates.add(cb.equal(root.get("size"), searchDto.getSize()));
        }

        if (searchDto.getCategoryId() != null) {
            predicates.add(cb.equal(root.get("category"), categoryService.getByIdEntity(searchDto.getCategoryId())));
        }

        if (searchDto.getDiscountId() != null) {
            predicates.add(cb.equal(root.get("discount"), discountService.getByIdEntity(searchDto.getDiscountId())));
        }

        query.where(predicates.toArray(new Predicate[0]));

        TypedQuery<Product> typedQuery = entityManager.createQuery(query);
        List<ProductDto> productDtoList = new ArrayList<>();
        for (Product product:typedQuery.getResultList()) {
            productDtoList.add(mapToDto(product));
        }
        return productDtoList;
    }


    @Override
    @Transactional
    public ProductDto update(Long id,@Valid ProductDto dto)
            throws ProductNotFoundException{

            Product  product = getByIdEntity(id);
            if(product==null){
                log.error("Метод update(Product), Exception: Продукт с id "+id+" не найден.");
                throw  new ProductNotFoundException("Продукт с id "+id+" не найден.");
            }

            if(dto.getName()!=null){
                product.setName(dto.getName());
            }
            if(dto.getPrice()!=null){
            product.setPrice(dto.getPrice());
        }
        if(dto.getSize()!=null){
            product.setSize(dto.getSize());
        }
        if(dto.getMaterial()!=null){
            product.setMaterial(dto.getMaterial());
        }
        if(dto.getCategory()!=null){
            product.setCategory(dto.getCategory());
        }
        if (dto.getDiscount()!=null){
            product.setDiscount(dto.getDiscount());
        }

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        if (!violations.isEmpty()) {

            List<String> errorMessages = new ArrayList<>();
            for (ConstraintViolation<Product> violation : violations) {
                errorMessages.add("Поле: " + violation.getPropertyPath() + " - " + violation.getMessage());
                log.warn("Метод update(Product): "+errorMessages);
            }
            throw new ValidException(errorMessages);
        }

        return mapToDto(productRepository.save(product));
    }

    @Override
    public String deleteById(Long id) throws ProductNotFoundException {
        try{
            Product product = getByIdEntity(id);
            product.setRdt(LocalDate.now());
            productRepository.save(product);
            return "Продукт с id: "+id+" был удален.";
        }catch (NullPointerException e){
            log.error("Метод deleteById(Product), Exception: Продукт с id "+id+" не найден.");
            throw new ProductNotFoundException("Продукт с id "+id+" не найден.");
        }

    }

    @Override
    public List<ProductDto> getAllByCategory(Long categoryId) throws ProductNotFoundException {
        List<Product> productList = productRepository.findAllByCategoryAndRdtIsNull(categoryId);
        if (productList.isEmpty()) {
            log.error("Метод getAllByCategory(Product), Exception: В базе нет товаров данной категории.");
            throw new ProductNotFoundException("В базе нет товаров данной категории.");
        }
        List<ProductDto> productDtoList = new ArrayList<>();
        for (Product product:productList) {
            productDtoList.add(mapToDto(product));
        }
        return productDtoList;
    }



    public String uploadImage(Long productId, MultipartFile file) throws ProductNotFoundException {
        try {

            Product product = getByIdEntity(productId);
            try {
                product.setImage(file.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            productRepository.save(product);
            return "Image saved to database successfully!";
        }catch (NullPointerException e){
            log.error("Метод uploadImage(Product), Exception: Продукт с id "+productId+" не найден.");
            throw new ProductNotFoundException("Продукт с id "+productId+" не найден.");
        }
    }


    public  byte[] getImageById(Long id) throws ProductNotFoundException {
        try {
            Product product = getByIdEntity(id);
            return product.getImage();
        }catch (NullPointerException e){
            log.error("Метод getImageById(Product), Exception: Продукт с id "+id+" не найден.");
            throw new ProductNotFoundException("Продукт с id "+id+" не найден.");
        }

    }
}
