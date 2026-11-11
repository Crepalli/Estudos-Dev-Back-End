# Estudos de desenvolvimento — Escola comentada

Um projeto de referência para estudar **Java → SQL/JDBC → JPA/Spring → Angular → JWT**.
O mesmo tema, cadastro de alunos e endereços, aparece em etapas independentes. Os comentários explicam as linhas importantes, os contratos e o motivo das decisões. Não é necessário ler tudo de uma vez.

## Comece aqui

1. Leia [o roteiro de estudo](docs/ROTEIRO.md).
2. Execute `01-console` e acompanhe uma operação de cadastro com o depurador.
3. Estude uma classe por vez, na ordem indicada abaixo.
4. Leia [os exercícios sem solução](docs/EXERCICIOS.md) e refaça o sistema em outra pasta.

## Organização

| Pasta | Assunto | Pré-requisitos |
|---|---|---|
| [01-console](01-console/src/estudo) | Java, objetos, interface, memória e validações | JDK 8 ou superior |
| [02-jdbc](02-jdbc) | O mesmo contrato de repositório implementado com SQL | JDK 17 e Maven 3.6.3+ |
| [03-api](03-api) | CRUD HTTP, JPA, DTOs, paginação, ViaCEP e JWT | JDK 17 e Maven 3.6.3+ |
| [04-frontend](04-frontend) | Formulários Angular, HTTP, autenticação e paginação | Node 22.12+ da linha 22 e npm |
| [docs](docs) | Leitura guiada, plano, exemplos HTTP e desafios | Leitura |

A versão de console usa matrícula como identidade. A API acrescenta ID gerado, e-mail e **um endereço por aluno**. A API é uma evolução independente: os dados não migram automaticamente entre as etapas. SQL/JDBC usa H2 próprio; a API usa outro banco H2 em sua pasta.

## 1. Executar o console

No PowerShell, a partir da raiz deste repositório:

```powershell
cd 01-console
.\executar.ps1
```

Se sua política do PowerShell impedir scripts, compile sem alterar a política:

```powershell
mkdir target -Force
javac -encoding UTF-8 -d target src/estudo/*.java Verificar.java
java -cp target Verificar
java -cp target estudo.Main
```

O menu cadastra, lista, busca, edita e remove. Os dados ficam somente em memória. Leia: `Aluno` → `AlunoRepository` → `AlunoEmMemoriaRepository` → `AlunoService` → `Main` → `Verificar`.

## 2. Executar JDBC

Abra outro terminal na raiz do repositório:

```powershell
cd 02-jdbc
mvn compile exec:java
```

A demonstração cadastra, edita, consulta e remove o aluno de matrícula `DEMO-JDBC`; uma execução posterior substitui esse registro demonstrativo, se existir. O banco fica em `02-jdbc/dados`. A pasta deve ser gravável. Leia o SQL e depois `AlunoJdbcRepository`. O plugin Maven inclui os modelos e contratos de `01-console`, por isso mantenha as pastas juntas.

## 3. Executar API

Confirme `java -version` e `mvn -version`: ambos precisam usar JDK 17 ou superior. O Java 8 não executa esta etapa. Defina uma senha local de pelo menos 12 caracteres (máximo 72 bytes UTF-8); não a coloque nos arquivos nem no GitHub.

```powershell
cd 03-api
$senhaLocal = Read-Host 'Escolha a senha local' -AsSecureString
$env:ESTUDO_SENHA = [System.Net.NetworkCredential]::new('', $senhaLocal).Password
mvn spring-boot:run
```

O usuário padrão é `estudante`. Para alterá-lo, defina `$env:ESTUDO_USUARIO`. A API atende em `http://127.0.0.1:8080`. A senha é necessária também ao empacotar para execução manual; os testes configuram uma senha exclusiva do ambiente de teste. Feche o terminal após o estudo para descartar as variáveis da sessão.

```powershell
mvn verify
```

Os testes verificam login, bloqueio sem token, CRUD, validações, matrícula repetida, matrícula imutável, limite de página, aluno inexistente e token inválido. Não chamam o ViaCEP.

Consulte [os exemplos HTTP](docs/API.md). O banco local é criado em `03-api/dados` e persiste após reiniciar. Não há console H2 web nem senha real incluída no repositório.

## 4. Executar a tela

Mantenha a API aberta e execute em outro terminal, com Node **22.12+ da linha 22**:

```powershell
cd 04-frontend
npm install
npm start
```

Abra `http://127.0.0.1:4200` e entre com o usuário e a senha configurados na API. O proxy do Angular encaminha `/auth` e `/api` para a porta 8080, dispensando CORS no desenvolvimento. `npm run build` verifica o TypeScript e gera a distribuição. Para publicar a distribuição em um servidor, será necessário configurar o mesmo encaminhamento no servidor; este projeto não faz implantação automática.

A tela oferece cadastro, edição, exclusão, consulta de CEP, paginação e mensagens de erro. O endereço pode ser preenchido manualmente quando o ViaCEP estiver indisponível. O JWT fica apenas na memória do navegador; atualizar a página exige login novamente.

## O que observar nos comentários

- **Modelo:** o que representa um aluno e quais dados não podem mudar?
- **Repositório:** como objetos entram e saem do armazenamento?
- **Serviço:** quais regras precisam passar antes de gravar?
- **Controller:** como uma requisição vira chamada de serviço e resposta HTTP?
- **Tela:** como eventos e formulários acionam o serviço HTTP?
- **Segurança:** como credenciais viram token e como o token autoriza uma requisição?

## Limites intencionais

Este é um laboratório local para uma única pessoa. O login usa um único usuário em memória e a chave JWT muda ao reiniciar. Não há cadastro de contas, permissões individuais por aluno, recuperação de senha, refresh token nem limitação de tentativas. O aluno cadastrado é um dado acadêmico, não uma conta de acesso. A API exige autenticação para todos os cadastros, mas todo usuário autenticado tem o mesmo acesso. A consulta externa tem timeout, mas ainda não possui teste automatizado de contrato. A compilação da tela não substitui um teste visual ou de navegador.

H2 e `ddl-auto=update` facilitam o estudo; migrações, banco PostgreSQL, múltiplos endereços, proteção contra atualizações simultâneas e autenticação de produção são exercícios posteriores. O exemplo JDBC usa `MERGE` específico do H2; ele não pode ser copiado para qualquer banco sem adaptação.

O workflow [Verificar projeto de estudo](.github/workflows/verificar.yml) compila o console, executa CRUD JDBC, testa a API e compila a tela a cada push. Os requisitos das ferramentas foram consultados na [documentação Spring Boot](https://docs.spring.io/spring-boot/3.5/system-requirements.html) e na [tabela de compatibilidade Angular](https://angular.dev/reference/versions).
