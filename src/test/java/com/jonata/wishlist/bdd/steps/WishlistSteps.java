package com.jonata.wishlist.bdd.steps;

import com.jonata.wishlist.application.WishlistService;
import com.jonata.wishlist.domain.entity.Wishlist;
import com.jonata.wishlist.domain.exception.BusinessException;
import com.jonata.wishlist.domain.exception.NotFoundException;
import com.jonata.wishlist.domain.exception.WishlistLimitExceededException;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class WishlistSteps {

    @Autowired
    private WishlistService wishlistService;

    private String customerId;
    private String productId;
    private Exception exception;
    private Wishlist wishlist;
    private boolean productPresent;

    @Dado("um cliente com ID {string}")
    public void umClienteComID(String customerId) {
        this.customerId = customerId;
    }

    @Dado("que o cliente não possui produtos na wishlist")
    public void clienteSemProdutos() {
        try {
            Wishlist existing = wishlistService.getWishlist(customerId);
            existing.getProducts().forEach(p ->
                    wishlistService.removeProduct(customerId, p.getProductId()));
        } catch (NotFoundException e) {
        }
    }

    @Dado("que o cliente já tem o produto {string} na wishlist")
    public void clienteComProdutoNaWishlist(String productId) {
        this.productId = productId;
        try {
            wishlistService.addProduct(customerId,
                    Wishlist.Product.builder()
                            .productId(productId)
                            .name("Produto Teste")
                            .price(100.0)
                            .build());
        } catch (Exception e) {
            fail("Falha ao configurar cenário: " + e.getMessage());
        }
    }

    @Dado("que o cliente já tem {int} produtos na wishlist")
    public void clienteComProdutosNaWishlist(int quantidade) {
        for (int i = 1; i <= quantidade; i++) {
            try {
                wishlistService.addProduct(customerId,
                        Wishlist.Product.builder()
                                .productId("prod-" + i)
                                .name("Produto " + i)
                                .price(i * 10.0)
                                .build());
            } catch (Exception e) {
                fail("Falha ao adicionar produto " + i + ": " + e.getMessage());
            }
        }
    }

    @Quando("o cliente adiciona o produto {string} na wishlist")
    public void adicionarProdutoNaWishlist(String productId) {
        this.productId = productId;
        try {
            this.wishlist = wishlistService.addProduct(customerId,
                    Wishlist.Product.builder()
                            .productId(productId)
                            .name("Novo Produto")
                            .price(50.0)
                            .build());
        } catch (WishlistLimitExceededException e) {
            this.exception = e;
        } catch (BusinessException e) {
            this.exception = e;
        }
    }

    @Quando("o cliente remove o produto {string} da wishlist")
    public void removerProdutoDaWishlist(String productId) {
        this.productId = productId;
        try {
            wishlistService.removeProduct(customerId, productId);
        } catch (NotFoundException e) {
            this.exception = e;
        }
    }

    @Quando("o cliente consulta sua wishlist")
    public void consultarWishlist() {
        try {
            this.wishlist = wishlistService.getWishlist(customerId);
        } catch (NotFoundException e) {
            this.exception = e;
        }
    }

    @Quando("o cliente verifica se o produto {string} está na wishlist")
    public void verificarProdutoNaWishlist(String productId) {
        this.productId = productId;
        this.productPresent = wishlistService.isProductInWishlist(customerId, productId);
    }

    @Entao("o produto deve ser adicionado com sucesso")
    public void produtoAdicionadoComSucesso() {
        assertNotNull(wishlist);
        assertTrue(wishlist.getProducts().stream()
                .anyMatch(p -> p.getProductId().equals(productId)));
    }

    @Entao("a wishlist deve conter {int} produto(s)")
    public void wishlistDeveConterQuantidadeDeProdutos(int quantidade) {
        assertEquals(quantidade, wishlist.getProducts().size());
    }

    @Entao("deve retornar erro de limite excedido")
    public void deveRetornarErroDeLimiteExcedido() {
        assertNotNull(exception);
        assertTrue(exception instanceof WishlistLimitExceededException);
    }

    @Entao("o produto deve ser removido com sucesso")
    public void produtoRemovidoComSucesso() {
        assertFalse(wishlistService.isProductInWishlist(customerId, productId));
    }

    @Entao("deve retornar erro de produto não encontrado")
    public void deveRetornarErroDeProdutoNaoEncontrado() {
        assertNotNull(exception);
        assertTrue(exception instanceof NotFoundException);
    }

    @Entao("a wishlist deve ser retornada com sucesso")
    public void wishlistRetornadaComSucesso() {
        assertNotNull(wishlist);
        assertEquals(customerId, wishlist.getCustomerId());
    }

    @Entao("o produto deve estar presente na wishlist")
    public void produtoDeveEstarPresente() {
        assertTrue(productPresent);
    }

    @Entao("o produto não deve estar presente na wishlist")
    public void produtoNaoDeveEstarPresente() {
        assertFalse(productPresent);
    }

    @Entao("deve retornar erro de produto já existente")
    public void deveRetornarErroDeProdutoJaExistente() {
        assertNotNull(exception);
        assertTrue(exception instanceof BusinessException);
    }

    @Dado("que o cliente já possui o produto {string} em sua lista")
    public void queOClienteJaPossuiOProdutoEmSuaLista(String productId) {
        this.productId = productId;
        try {
            wishlistService.addProduct(customerId,
                    Wishlist.Product.builder()
                            .productId(productId)
                            .name("Produto Existente")
                            .price(50.0)
                            .build());
        } catch (Exception e) {
            fail("Falha ao configurar produto existente");
        }
    }

    @Quando("eu tento adicionar o mesmo produto {string} novamente")
    public void euTentoAdicionarOMesmoProdutoNovamente(String productId) {
        this.productId = productId;
        try {
            wishlistService.addProduct(customerId,
                    Wishlist.Product.builder()
                            .productId(productId)
                            .name("Produto Duplicado")
                            .price(60.0)
                            .build());
        } catch (BusinessException e) {
            this.exception = e;
        }
    }
}