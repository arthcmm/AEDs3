# **TP2 - Relacionamento 1:N**
**Grupo**: Arthur Castro, Matheus Campbell e Pedro Gaioso

## **Descrição do Projeto**

O projeto consiste em uma base de dados CRUD (Create, Read, Update, Delete) genérica em Java, criada para gerenciar a entidade **Tarefa** com relacionamentos entre entidades, especialmente entre **Tarefa** e **Categoria** em uma relação 1:N. O armazenamento e busca de dados são otimizados por meio de estruturas indexadas, usando uma tabela hash extensível e uma Árvore B+.

Cada tarefa possui uma categoria, e a Árvore B+ permite associar várias tarefas a uma mesma categoria, viabilizando o gerenciamento eficiente das tarefas por classificação.

As **Tarefas** têm os seguintes atributos:
- **ID**: Identificador único gerado automaticamente.
- **Nome**: Nome da tarefa.
- **ID de Categoria**: Referência à categoria da tarefa (chave estrangeira).
- **Data de Início**: Data em que a tarefa foi iniciada.
- **Data de Conclusão**: Data em que a tarefa foi concluída.
- **Status**: Estado atual da tarefa (Pendente, Em Progresso, Concluída, etc.).

## **Estrutura do Projeto**

O projeto possui as seguintes classes principais:

### **1. Interface Registro**

- **Descrição**: Define os métodos necessários para um objeto ser armazenado e recuperado de um arquivo binário.
- **Métodos**:
  - `int getID()`: Retorna o ID do registro.
  - `void setID(int id)`: Define o ID do registro.
  - `byte[] toByteArray() throws IOException`: Serializa o objeto para um array de bytes.
  - `void fromByteArray(byte[] ba) throws IOException`: Desserializa o objeto a partir de um array de bytes.

### **2. Interface RegistroHashExtensivel**

- **Descrição**: Interface para que um objeto seja usado na tabela hash extensível.
- **Métodos**:
  - `int hashCode()`: Retorna o código hash do objeto.
  - `short size()`: Retorna o tamanho em bytes do objeto.
  - `byte[] toByteArray() throws IOException`: Serializa o objeto.
  - `void fromByteArray(byte[] ba) throws IOException`: Desserializa o objeto.

### **3. Classe Tarefa**

- **Descrição**: Representa a entidade Tarefa, implementando as interfaces `Registro` e `RegistroHashExtensivel<Tarefa>`.
- **Atributos**:
  - `int id`: ID da tarefa.
  - `String nome`: Nome da tarefa.
  - `int idCategoria`: ID da categoria da tarefa.
  - `String dataInicio`: Data de início.
  - `String dataConclusao`: Data de conclusão.
  - `String status`: Status da tarefa.
- **Métodos**: Construtores, getters, setters, `toString()` e implementação dos métodos das interfaces.

### **4. Classe Categoria**

- **Descrição**: Representa a entidade Categoria, permitindo a classificação de tarefas.
- **Atributos**:
  - `int id`: ID da categoria.
  - `String nome`: Nome da categoria.
- **Métodos**: Construtores, getters e setters para manipular as categorias.

### **5. Classe ParChaveEndereco**

- **Descrição**: Representa um par chave (hashCode) e endereço, utilizado para indexação com a tabela hash extensível.
- **Atributos**: `int chave` (ID) e `long endereco` (local no arquivo).
- **Métodos**: Construtores, getters, e implementação da interface `RegistroHashExtensivel<ParChaveEndereco>`.

### **6. Classe HashExtensivel<T extends RegistroHashExtensivel<T>>**

- **Descrição**: Implementação da tabela hash extensível, utilizada para indexação direta.
- **Métodos**:
  - `boolean create(T elemento)`: Insere um novo par chave-endereço.
  - `T read(int chave)`: Recupera o par chave-endereço pelo ID.
  - `boolean update(T elemento)`: Atualiza o endereço associado ao ID.
  - `boolean delete(int chave)`: Remove o par chave-endereço.

### **7. Classe ArvoreBMais<T extends RegistroArvoreBMais<T>>**

- **Descrição**: Implementação da Árvore B+ para indexação indireta, permitindo busca por atributos não únicos.
- **Métodos**: 
  - Operações CRUD e busca baseadas em UID para armazenar e relacionar categorias com tarefas associadas.

### **8. Classe Arquivo<T extends Registro & RegistroHashExtensivel<T>>**

- **Descrição**: Gerencia operações CRUD em um arquivo binário com um índice direto (hash extensível).
- **Métodos**:
  - `int create(T objeto)`: Insere um novo registro e atualiza o índice.
  - `T read(int id)`: Lê um registro via índice.
  - `boolean update(T objeto)`: Atualiza um registro existente.
  - `boolean delete(int id)`: Exclui um registro e remove do índice.

### **9. Classe IO**

- **Descrição**: Contém o `main` para execução do sistema, ilustrando o uso das operações CRUD com tarefas e categorias.
- **Funcionalidades**:
  - Criação e gerenciamento de tarefas e categorias.
  - Testes das operações de CRUD no console.

## **Relato da Experiência**

**Desafios Encontrados**
- **Dificuldade de Implementação**: A implementação do relacionamento 1:N usando a Árvore B+ foi a operação mais complexa, pois exigiu integração cuidadosa com o índice.

**Resultados Alcançados**
- O sistema gerencia tarefas e categorias com eficiência, aproveitando o índice direto para acesso rápido e o índice indireto (Árvore B+) para a listagem de tarefas por categoria.
- As operações de busca e gerenciamento de relacionamento 1:N são eficazes.

## **Checklist**

- **O CRUD (com índice direto) de categorias foi implementado?**  
  - **Resposta**: Sim
- **Há um índice indireto de nomes para as categorias?**
  - **Resposta**: Sim, com Árvore B+
- **O atributo de ID de categoria, como chave estrangeira, foi criado na classe Tarefa?**
  - **Resposta**: Sim
- **É possível listar as tarefas de uma categoria?**
  - **Resposta**: Sim
- **A remoção de categorias checa se há alguma tarefa vinculada a ela?**
  - **Resposta**: Sim
- **A inclusão da categoria em uma tarefa se limita às categorias existentes?**
  - **Resposta**: Sim
- **O trabalho está funcionando corretamente?**
  - **Resposta**: Sim
- **O trabalho é original e não a cópia de um trabalho de outro grupo?**
  - **Resposta**: Sim

## **Considerações Finais**

Este projeto permitiu explorar e aprofundar conhecimentos sobre estruturação de dados complexos, manipulando arquivos indexados e entendendo a eficiência de uma Árvore B+ e uma tabela hash extensível. A utilização dessas estruturas facilita o gerenciamento escalável e eficiente de dados.

**Possíveis Melhorias**
- Adicionar tratamento de exceções para feedback ao usuário.
- Desenvolver uma interface gráfica para facilitar o uso.
- Arrumar listagem de categorias