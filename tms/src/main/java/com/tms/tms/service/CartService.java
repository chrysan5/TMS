package com.tms.tms.service;

import com.tms.tms.dto.CartProductListDto;
import com.tms.tms.dto.CartProductRequestDto;
import com.tms.tms.exception.ErrorCode;
import com.tms.tms.exception.TmsCustomException;
import com.tms.tms.model.Cart;
import com.tms.tms.model.CartProduct;
import com.tms.tms.model.Product;
import com.tms.tms.repository.CartProductRepository;
import com.tms.tms.repository.CartRepository;
import com.tms.tms.repository.ProductRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@AllArgsConstructor
@Service
public class CartService {
    private final CartRepository cartRepository;
    private final CartProductRepository cartProductRepository;
    private final ProductService productService;
    private final ProductRepository productRepository;


    @Transactional
    public void putProductInCart(@Valid CartProductRequestDto cartProductRequestDto, String username) {

        // 유저의 장바구니 찾은 후 존재하지 않으면 새로 생성
        Cart cart = cartRepository.findByUsername(username)
                .orElseGet(() -> cartRepository.save(new Cart(username)));

        Product product = productService.findByIdOrElseThrow(cartProductRequestDto.getProductId());

        //장바구니에 상품이 있는지 없는지 확인 상품이 없으면 상품 0개 추가로 장바구니_상품 생성, 이후 수량 증가
        CartProduct cartProduct = cartProductRepository.findByCartAndProduct(cart, product)
                .orElseGet(() -> new CartProduct(cart, product, 0));

        cartProduct.setQuantity(cartProduct.getQuantity() + cartProductRequestDto.getQuantity());
        cartProductRepository.save(cartProduct);

    }

    @Transactional
    public void updateProductInCart(@Valid CartProductRequestDto cartProductRequestDto, String username) {
        Cart cart = findByUsernameOrElseThrow(username);
        Product product = productService.findByIdOrElseThrow(cartProductRequestDto.getProductId());

        CartProduct cartProduct = cartProductRepository.findByCartAndProduct(cart, product)
                .orElseThrow(() -> new TmsCustomException(ErrorCode.NOT_FOUND_CART_PRODUCT));

        cartProduct.setQuantity(cartProductRequestDto.getQuantity());
    }

    @Transactional
    public void removeProductInCart(List<Long> productIds, String username) {
        Cart cart = findByUsernameOrElseThrow(username);
        List<Product> products = productRepository.findByProductIdIn(productIds);
        List<CartProduct> cartProducts = cartProductRepository.findByCartAndProductIn(cart, products);

        if (cartProducts.isEmpty()) {
            throw new RuntimeException("장바구니에 해당 상품이 없습니다.");
        }

        // 장바구니에서 해당 상품들 삭제
        cartProductRepository.deleteAll(cartProducts);

        // 장바구니에 상품이 비었는지 확인 후 삭제 //SELECT EXISTS (SELECT 1 FROM cart_product WHERE cart_id = ?)
        if (!cartProductRepository.existsByCart(cart)) {
            cartRepository.delete(cart);
        }
    }

    @Transactional
    public void removeCart(String username) {
        Cart cart = findByUsernameOrElseThrow(username);
        List<CartProduct> cartProducts = cartProductRepository.findAllByCart(cart);

        cartProductRepository.deleteAll(cartProducts);
        cartRepository.delete(cart);
    }

    @Transactional(readOnly = true)
    public Page<CartProductListDto> getCart(int page, int size, boolean isAsc, String username) {
        Pageable pageable = PageRequest.of(page, size);

        Cart cart = findByUsernameOrElseThrow(username);
        Page<CartProduct> cartProducts = cartProductRepository.findAllByCart(cart, pageable);
        return cartProducts.map(CartProductListDto::new);
    }


    public Cart findByUsernameOrElseThrow(String username){
        return cartRepository.findByUsername(username).orElseThrow(
                () -> new TmsCustomException(ErrorCode.NOT_FOUND_CART)
        );
    }


}
