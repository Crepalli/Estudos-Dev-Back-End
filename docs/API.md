# Experimentar a API no PowerShell

Com a API iniciada, abra outro PowerShell. Os exemplos não incluem credenciais reais.

## Login

```powershell
$base = 'http://127.0.0.1:8080'
$segredo = Read-Host 'Senha configurada na API' -AsSecureString
$senha = [System.Net.NetworkCredential]::new('', $segredo).Password
$credenciais = @{ usuario = 'estudante'; senha = $senha } | ConvertTo-Json
$login = Invoke-RestMethod -Method Post -Uri "$base/auth/login" -ContentType 'application/json' -Body $credenciais
$headers = @{ Authorization = "Bearer $($login.accessToken)" }
$senha = $null
$credenciais = $null
```

O token expira em 15 minutos. Faça login novamente após expiração ou reinicialização da API.

## Cadastrar

```powershell
$aluno = @{
    matricula = '2026-001'
    nome = 'Ana Maria'
    email = 'ana@example.com'
    endereco = @{
        cep = '01001000'
        logradouro = 'Praça da Sé'
        numero = '10'
        cidade = 'São Paulo'
        uf = 'SP'
    }
}
$corpo = $aluno | ConvertTo-Json -Depth 3
$criado = Invoke-RestMethod -Method Post -Uri "$base/api/alunos" -Headers $headers -ContentType 'application/json; charset=utf-8' -Body ([Text.Encoding]::UTF8.GetBytes($corpo))
$id = $criado.id
```

## Listar, buscar e consultar CEP

```powershell
Invoke-RestMethod -Uri "$base/api/alunos?pagina=0&tamanho=10" -Headers $headers
Invoke-RestMethod -Uri "$base/api/alunos/$id" -Headers $headers
Invoke-RestMethod -Uri "$base/api/ceps/01001000" -Headers $headers
```

CEP exige internet; CRUD local não exige depois que as dependências estiverem instaladas. A resposta de listagem contém `content`, `totalElements` e `totalPages`; a primeira página é zero.

## Editar e excluir

```powershell
$aluno.nome = 'Ana Maria Silva'
$corpo = $aluno | ConvertTo-Json -Depth 3
Invoke-RestMethod -Method Put -Uri "$base/api/alunos/$id" -Headers $headers -ContentType 'application/json; charset=utf-8' -Body ([Text.Encoding]::UTF8.GetBytes($corpo))
Invoke-RestMethod -Method Delete -Uri "$base/api/alunos/$id" -Headers $headers
```

PUT recebe todos os campos e mantém a matrícula. DELETE remove também o endereço embutido, pois ele pertence ao mesmo registro.

| Situação | Código |
|---|---|
| Cadastro criado | 201 |
| Consulta ou edição bem-sucedida | 200 |
| Exclusão bem-sucedida | 204 |
| Campo/parâmetro inválido ou tentativa de mudar matrícula | 400 |
| Sem token, token inválido ou credenciais incorretas | 401 |
| Aluno/CEP inexistente | 404 |
| Matrícula já cadastrada | 409 |
| Serviço externo de CEP indisponível | 502 |

Ao receber um erro HTTP, o PowerShell gera uma exceção: isso é esperado nos exercícios de erro. Consulte o corpo da resposta pelo cliente HTTP ou pela tela para ler `detail` e, quando presente, `campos`.
