# CRUD de Notas Fiscais

Aplicação desktop em **Java 21 + JavaFX**, com banco **MySQL** acessado via JDBC, para cadastrar, pesquisar, atualizar e remover notas fiscais.

## Como rodar

### Pré-requisitos
- JDK 21
- MySQL rodando em `localhost:3306`

O banco `notas_db`, a tabela `notas_fiscais` e a procedure `sp_inserir_nota_fiscal` são criados automaticamente ao conectar. Não é preciso rodar nenhum script antes.

### 1. Configure as variáveis de ambiente

A senha do banco **não fica mais no código**. O `ResourceManager` lê as credenciais de variáveis de ambiente com `System.getenv`:

| Variável      | Obrigatória | Valor padrão                                                        |
|---------------|-------------|---------------------------------------------------------------------|
| `DB_PASSWORD` | sim         | nenhum                                                              |
| `DB_USER`     | não         | `root`                                                              |
| `DB_URL`      | não         | `jdbc:mysql://localhost:3306/notas_db?createDatabaseIfNotExist=true` |

Para definir a senha, escolha **uma** das opções abaixo.

**Só no terminal atual (PowerShell).** A variável some quando o terminal é fechado:
```powershell
$env:DB_PASSWORD = "sua_senha"
.\gradlew.bat run
```

**De forma permanente, para o seu usuário do Windows:**
```powershell
setx DB_PASSWORD "sua_senha"
```
O `setx` só vale para terminais abertos depois. Feche e reabra o terminal e o VS Code.

Também dá para fazer pela interface do Windows: menu Iniciar → "variáveis de ambiente" → *Editar as variáveis de ambiente para sua conta* → **Novo**.

**No VS Code, pelo botão Run/Debug.** Use o `.vscode/launch.json`:
```json
{
  "type": "java",
  "name": "Tela",
  "request": "launch",
  "mainClass": "edu.curso.boundary.Tela",
  "env": { "DB_PASSWORD": "sua_senha" }
}
```
> Esse arquivo guarda a senha. Não faça commit dele: adicione `.vscode/launch.json` ao `.gitignore`.

**Linux/macOS:**
```bash
export DB_PASSWORD="sua_senha"
./gradlew run
```

#### Erros comuns
- **`Access denied for user 'root'@'localhost' (using password: NO)`**: o Java não encontrou a `DB_PASSWORD`. Confira se ela foi definida no mesmo terminal ou se você reiniciou o terminal/VS Code depois do `setx`.
- Mudou o valor da variável? Reinicie a aplicação. Ela só é lida quando o processo inicia.

### 2. Execute
```powershell
.\gradlew.bat run
```

## Branch `gabriel/procedure`

Esta branch adiciona uma **segunda forma de inserir notas**, usando uma stored procedure do MySQL.

### O que mudou
- **Modal ao clicar em "Adicionar"** (`Tela.java`): agora o usuário escolhe entre:
  - **Padrão**: `INSERT` direto pelo Java, com `PreparedStatement`. A checagem de nota duplicada é feita no Java, pelo `notaExiste`.
  - **Via Procedure**: chama `sp_inserir_nota_fiscal` com `CallableStatement`. As regras ficam **no banco**.
- **Procedure `sp_inserir_nota_fiscal`**: valida os dados antes de inserir e recusa a nota com `SIGNAL SQLSTATE '45000'` quando:
  - o valor total é menor ou igual a zero;
  - a data de vencimento é anterior à de emissão;
  - já existe uma nota com o mesmo número e fornecedor.

  A mensagem do `SIGNAL` chega ao Java como `SQLException` e é exibida na tela.
- **Criação automática da procedure** (`ResourceManager.criarProcedure`): a cada conexão a procedure é removida e criada de novo (`DROP PROCEDURE IF EXISTS` + `CREATE`). Assim, qualquer alteração no código já vale na próxima execução.
- **Credenciais via variáveis de ambiente**, como descrito acima.
- `SQL/V1__create_procedure.sql`: o script da procedure, para consulta ou para rodar manualmente no Workbench.

### Fluxo da inserção via procedure
```
Tela (modal "Via Procedure")
  └─ NotaFiscalController.adicionarViaProcedure(nf)   → devolve "" ou a mensagem de erro
       └─ NotaFiscalDAOImplementation.adicionarViaProcedure(nf)
            └─ {CALL sp_inserir_nota_fiscal(?, ?, ?, ?, ?, ?)}   → MySQL valida e insere
```

### Conceitos usados
- **`CallableStatement`**: subinterface de `PreparedStatement` feita para chamar procedures e functions (`con.prepareCall(...)`). Além de parâmetros `IN`, aceita parâmetros `OUT`/`INOUT`, registrados com `registerOutParameter`.
- **`{CALL ...}`**: sintaxe de escape do JDBC. O driver traduz a chamada para a sintaxe do banco em uso, o que deixa o código Java portável.
- **`execute()` x `executeUpdate()`**: `executeUpdate()` devolve o número de linhas afetadas, para INSERT/UPDATE/DELETE. `execute()` é genérico: devolve `true` se o resultado for um `ResultSet`. É o mais indicado para procedures, que podem devolver resultados variados.
- **`DELIMITER`** só é necessário em clientes como o Workbench. Pelo JDBC cada `execute()` já envia um comando completo.
