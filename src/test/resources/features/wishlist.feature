@wishlist
Funcionalidade: Gerenciamento de Lista de Desejos
  Como um cliente de e-commerce
  Eu quero gerenciar minha lista de desejos
  Para poder salvar produtos que desejo comprar depois

  Contexto:
    Dado um cliente com ID "cli-123"

  @adicionar_produto
  Cenário: Adicionar produto à lista vazia
    Dado que o cliente não tem produtos na lista de desejos
    Quando ele adiciona o produto "prod-001" chamado "Smartphone" com preço 1500.00
    Então o produto deve ser adicionado com sucesso
    E a lista deve conter exatamente 1 produto

  @produto_duplicado
  Cenário: Tentar adicionar produto repetido
    Dado que o cliente já tem o produto "prod-001" na lista
    Quando ele tenta adicionar o produto "prod-001" novamente
    Então deve retornar o erro "Produto já existe na lista"

  @limite_produtos
  Cenário: Tentar exceder o limite de produtos
    Dado que o cliente já tem 20 produtos na lista
    Quando ele tenta adicionar o produto "prod-021"
    Então deve retornar o erro "Limite máximo de 20 produtos atingido"

  @remover_produto
  Cenário: Remover produto existente
    Dado que o cliente tem o produto "prod-001" na lista
    Quando ele remove o produto "prod-001"
    Então o produto deve ser removido com sucesso
    E a lista deve ficar vazia

  @consultar_lista
  Cenário: Consultar lista completa
    Dado que o cliente tem 3 produtos na lista
    Quando ele visualiza sua lista de desejos
    Então deve retornar uma lista com os 3 produtos

  @verificar_produto
  Cenário: Verificar se produto está na lista
    Dado que o cliente tem o produto "prod-001" na lista
    Quando ele verifica se o produto "prod-001" está presente
    Então deve retornar "true"

  @produto_inexistente
  Cenário: Verificar produto que não está na lista
    Dado que o cliente não tem produtos na lista
    Quando ele verifica se o produto "prod-999" existe
    Então deve retornar "false"