package com.jonata.wishlist.infrastructure.api;

import com.jonata.wishlist.application.WishlistService;
import com.jonata.wishlist.domain.entity.Wishlist;
import com.jonata.wishlist.infrastructure.dto.ProductPresenceResponse;
import com.jonata.wishlist.infrastructure.dto.ProductRequest;
import com.jonata.wishlist.infrastructure.dto.ProductResponse;
import com.jonata.wishlist.infrastructure.dto.WishlistResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
@Tag(name = "Wishlist", description = "API para gerenciamento de lista de desejos")
public class WishlistController {

    private final WishlistService wishlistService;

    @Operation(summary = "Adicionar produto à wishlist", description = "Adiciona um novo produto à lista de desejos do cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Produto adicionado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
            @ApiResponse(responseCode = "422", description = "Limite de produtos na wishlist excedido")
    })
    @PostMapping("/{customerId}/products")
    public ResponseEntity<WishlistResponse> addProduct(
            @Parameter(description = "ID do cliente", required = true, example = "12345")
            @PathVariable String customerId,
            @RequestBody @Valid ProductRequest productRequest) {

        var product = Wishlist.Product.builder()
                .productId(productRequest.getProductId())
                .name(productRequest.getName())
                .price(productRequest.getPrice())
                .build();

        var wishlist = wishlistService.addProduct(customerId, product);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(wishlist));
    }

    @Operation(summary = "Remover produto da wishlist", description = "Remove um produto da lista de desejos do cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Produto removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente ou produto não encontrado")
    })
    @DeleteMapping("/{customerId}/products/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeProduct(
            @Parameter(description = "ID do cliente", required = true, example = "12345")
            @PathVariable String customerId,
            @Parameter(description = "ID do produto", required = true, example = "prod-001")
            @PathVariable String productId) {
        wishlistService.removeProduct(customerId, productId);
    }

    @Operation(summary = "Obter wishlist", description = "Retorna todos os produtos da lista de desejos do cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Wishlist retornada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    @GetMapping("/{customerId}")
    public ResponseEntity<WishlistResponse> getWishlist(
            @Parameter(description = "ID do cliente", required = true, example = "12345")
            @PathVariable String customerId) {
        return ResponseEntity.ok(toResponse(wishlistService.getWishlist(customerId)));
    }

    @Operation(summary = "Verificar produto na wishlist", description = "Verifica se um produto está na lista de desejos do cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Verificação realizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    @GetMapping("/{customerId}/products/{productId}")
    public ResponseEntity<ProductPresenceResponse> checkProduct(
            @Parameter(description = "ID do cliente", required = true, example = "12345")
            @PathVariable String customerId,
            @Parameter(description = "ID do produto", required = true, example = "prod-001")
            @PathVariable String productId) {
        return ResponseEntity.ok(
                ProductPresenceResponse.builder()
                        .present(wishlistService.isProductInWishlist(customerId, productId))
                        .build()
        );
    }

    private WishlistResponse toResponse(Wishlist wishlist) {
        return WishlistResponse.builder()
                .customerId(wishlist.getCustomerId())
                .products(wishlist.getProducts().stream()
                        .map(p -> ProductResponse.builder()
                                .productId(p.getProductId())
                                .name(p.getName())
                                .price(p.getPrice())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}