# Roteiro de leitura e estudo

## Dois blocos diários de 1h30

**Bloco 1:** 40 min de conceito, 10 min de pausa, 30 min de leitura/depuração e 10 min de anotações.
**Bloco 2:** 15 min para planejar um exercício, 50 min para implementar, 10 min de pausa e 15 min para verificar o resultado.

Ao terminar, responda: o que entra, quem valida, quem armazena, o que sai e o que acontece quando algo dá errado? Anote as respostas com suas palavras.

## Primeiros cinco dias — 11 a 15/11/2026

| Dia | Bloco 1 | Bloco 2 |
|---|---|---|
| Quarta | Variáveis, condições, laços e métodos | Executar o menu e desenhar seu fluxo |
| Quinta | `Aluno`: objetos, construtor e encapsulamento | Criar uma classe Livro sem copiar Aluno |
| Sexta | `Map`, `List`, `Optional` e exceções | Explicar matrícula duplicada e adicionar uma regra de nome |
| Sábado | Interface, implementação e injeção por construtor | Trocar o repositório em memória por uma implementação própria de exercício |
| Domingo | SQL e introdução ao JDBC, se a base estiver firme | Ler SELECT/INSERT/UPDATE/DELETE e executar a demonstração JDBC |

Se for seu primeiro contato com Java, mantenha o domingo no console. As datas organizam o tempo; compreensão determina o avanço.

## Semanas seguintes

| Período | Foco | Entrega do seu exercício |
|---|---|---|
| 16–22/11 | SQL, chaves, JDBC e recursos | CRUD em banco e explicação de PreparedStatement |
| 23–29/11 | JPA, composição e consulta externa | Mapear entidades e tratar indisponibilidade de CEP |
| 30/11–06/12 | HTTP, Spring, DTOs e validação | Recriar endpoints e documentar seus códigos de resposta |
| 07–13/12 | HTML, CSS, JS, TypeScript e Angular | Formulário e lista com dados em memória |
| 14–20/12 | Cliente HTTP, paginação e estados da tela | Conectar sua tela à API |
| 21–27/12 | Hash, autenticação, JWT e testes | Login, rotas protegidas e verificações de erro |

Na API a segurança já está ligada. Para estudar CRUD antes de entender JWT, siga o login pronto em `API.md` e use o token; não precisa desmontar os filtros para testar.

## Leia por fluxos, não por quantidade de arquivos

### Cadastro no console
`Main` → `AlunoService.cadastrar` → `Aluno` → `AlunoRepository.salvar` → `AlunoEmMemoriaRepository`.

### Escrita em banco
`MainJdbc` → o mesmo `AlunoService` → `AlunoJdbcRepository` → `PreparedStatement` → H2.

### Cadastro pela API
`AlunoController` → validação de `AlunoRequest` → `AlunoService` → `AlunoRepository` → JPA/H2 → `AlunoResponse`.

### Consulta pela tela
Evento no HTML → método de `App` → `EscolaService` → interceptor do token → proxy → filtro JWT → controller → banco → JSON → componente → HTML.

### Consulta de CEP
Botão → API `/api/ceps` → validação de oito dígitos → `ViaCepService` → serviço externo → preenchimento dos campos. O endereço ainda precisa ser salvo pelo cadastro.

## Pequeno glossário

| Conceito | Significado neste projeto |
|---|---|
| Encapsulamento | Impedir que outras classes alterem diretamente o estado |
| Interface | Contrato que permite várias implementações de armazenamento |
| Injeção de dependência | Receber o colaborador em vez de criá-lo dentro da classe |
| DTO | Formato de entrada/saída da API, separado da persistência |
| Transação | Unidade de trabalho confirmada ou desfeita em conjunto |
| ORM/JPA | Mapeamento entre objetos Java e banco relacional |
| Autenticação | Confirmar quem está fazendo a requisição |
| Autorização | Decidir o que essa identidade pode acessar |
| Hash | Representação usada para verificar uma senha sem guardar a original |
| JWT | Token assinado e com expiração; seu conteúdo não é criptografado por padrão |
| Observable | Representação usada pelo cliente Angular para acompanhar uma resposta assíncrona |

## Perguntas para o depurador

1. Onde a execução para quando a matrícula é repetida?
2. Por que trocar a implementação do repositório não exige mudar o serviço do console?
3. Quem fecha Connection, PreparedStatement e ResultSet?
4. O que acontece quando duas requisições tentam cadastrar a mesma matrícula?
5. Por que o número do endereço não é um inteiro?
6. Onde um token expirado é rejeitado antes de chegar ao controller?
7. Por que a tela copia o endereço antes de editar?
8. Por que validação no navegador não substitui validação no servidor?
