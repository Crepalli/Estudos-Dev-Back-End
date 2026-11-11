$ErrorActionPreference = 'Stop'
Push-Location $PSScriptRoot # Garante que os caminhos funcionem mesmo se o terminal estiver em outra pasta.
try {
    New-Item -ItemType Directory -Force target | Out-Null
    $fontes = @(Get-ChildItem -LiteralPath src -Filter *.java -Recurse | ForEach-Object FullName)
    & javac -encoding UTF-8 -d target @fontes Verificar.java
    if ($LASTEXITCODE -ne 0) { throw 'Falha ao compilar.' }
    & java -cp target Verificar # Verifica as regras antes de abrir o cadastro.
    if ($LASTEXITCODE -ne 0) { throw 'Falha nas verificações.' }
    & java -cp target estudo.Main
} finally { Pop-Location }
