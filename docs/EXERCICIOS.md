# Seu projeto do zero — sem solução pronta

Use outra pasta ou outro repositório. Escolha **biblioteca e empréstimos** para praticar os mesmos conceitos com um domínio diferente.

## Etapa 1 — Java

- Modele Livro com código, título e autor.
- Impeça código repetido e título vazio.
- Implemente cadastrar, listar, buscar, editar e remover no console.
- Separe entrada de teclado, regras e armazenamento.
- Crie um contrato de repositório e sua implementação em memória.
- Verifique que uma edição inválida não modifica os dados anteriores.

**Critério de avanço:** explique cada classe sem consultar este projeto.

## Etapa 2 — Banco

- Crie tabelas para livros, leitores e empréstimos.
- Use chaves estrangeiras e restrições.
- Implemente consultas parametrizadas com JDBC.
- Registre um empréstimo somente se houver exemplar disponível.
- Use transação para que atualização do estoque e empréstimo aconteçam juntos.

**Critério de avanço:** explique o que acontece se a segunda escrita da transação falhar.

## Etapa 3 — API

- Recrie o cadastro com JPA e Spring.
- Defina DTOs e valide os campos.
- Diferencie 201, 204, 400, 404 e 409.
- Implemente paginação.
- Teste cadastro repetido, inexistente e dados inválidos.

## Etapa 4 — Tela

- Crie primeiro uma tela com dados em memória.
- Depois conecte à API usando um serviço separado.
- Apresente carregamento, ausência de dados e falhas.
- Preserve o formulário quando salvar falhar.

## Etapa 5 — Segurança

- Proteja a API e implemente login.
- Nunca devolva senha ou hash em respostas de cadastro.
- Diferencie bibliotecário e leitor; um leitor só consulta seus próprios empréstimos.
- Teste token inválido e acesso sem permissão.

## Desafios adicionais no projeto de alunos

1. Faça uma busca de aluno por nome, paginada.
2. Adicione testes de ViaCEP com respostas simuladas: sucesso, CEP inexistente e timeout.
3. Troque Endereco embutido por uma relação de vários endereços por aluno.
4. Adicione migrações de banco e depois suporte a PostgreSQL.
5. Use controle de versão otimista para impedir que uma edição sobrescreva outra silenciosamente.
6. Adicione testes de navegador para login, cadastro, edição e exclusão.

Não tente resolver todos de uma vez: escolha o desafio correspondente ao assunto da semana.
