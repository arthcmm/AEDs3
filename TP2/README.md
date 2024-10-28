# **TP2 - Relacionamento 1:N**
Grupo: Arthur Castro, Matheus Campbell e Pedro Gaioso
## **Descrição do Projeto**

O projeto implementa um sistema CRUD (Create, Read, Update, Delete) genérico em Java para gerenciar entidades, com foco na entidade **Tarefa**. O sistema permite a inclusão, leitura, atualização e exclusão de tarefas em um arquivo binário. Além disso, implementa um índice direto utilizando uma tabela hash extensível para otimizar as operações de busca, conforme especificado pelo professor.

As tarefas gerenciadas possuem os seguintes atributos:

- **ID**: Identificador único sequencial gerado automaticamente.
- **Nome**: Nome da tarefa.
- **Data de Criação**: Data em que a tarefa foi criada.
- **Data de Conclusão**: Data em que a tarefa foi concluída.
- **Status**: Estado atual da tarefa (Pendente, Em Progresso, Concluída, etc.).
- **Prioridade**: Nível de prioridade da tarefa (Alta, Média, Baixa).

## **Estrutura do Projeto**

O projeto é composto pelas seguintes classes:

### **1. Interface Registro**

- **Descrição**: Interface que define os métodos necessários para que um objeto seja armazenado e recuperado de um arquivo binário.
- **Métodos**:
  - `int getID()`: Retorna o ID do registro.
  - `void setID(int id)`: Define o ID do registro.
  - `byte[] toByteArray() throws IOException`: Serializa o objeto em um array de bytes.
  - `void fromByteArray(byte[] ba) throws IOException`: Desserializa o objeto a partir de um array de bytes.

### **2. Interface RegistroHashExtensivel**

- **Descrição**: Interface que define os métodos necessários para que um objeto seja utilizado na tabela hash extensível.
- **Métodos**:
  - `int hashCode()`: Retorna o código hash do objeto (utilizado como chave).
  - `short size()`: Retorna o tamanho em bytes do objeto.
  - `byte[] toByteArray() throws IOException`: Serializa o objeto em um array de bytes.
  - `void fromByteArray(byte[] ba) throws IOException`: Desserializa o objeto a partir de um array de bytes.

### **3. Classe Tarefa**

- **Descrição**: Representa a entidade Tarefa, implementando as interfaces `Registro` e `RegistroHashExtensivel<Tarefa>`.
- **Atributos**:
  - `int id`: Identificador único da tarefa.
  - `String nome`: Nome da tarefa.
  - `String dataCriacao`: Data de criação da tarefa.
  - `String dataConclusao`: Data de conclusão da tarefa.
  - `String status`: Status da tarefa.
  - `String prioridade`: Prioridade da tarefa.
- **Principais Métodos**:
  - Construtores (padrão e com parâmetros).
  - Getters e setters para cada atributo.
  - Implementação dos métodos das interfaces `Registro` e `RegistroHashExtensivel<Tarefa>`.
  - `String toString()`: Representação textual da tarefa.

### **4. Classe ParChaveEndereco**

- **Descrição**: Classe auxiliar que representa um par de chave (hashCode) e endereço, utilizada pela tabela hash extensível.
- **Atributos**:
  - `int chave`: Chave (ID da tarefa).
  - `long endereco`: Endereço do registro no arquivo.
- **Métodos**:
  - Construtores (padrão e com parâmetros).
  - Getters para os atributos.
  - Implementação dos métodos da interface `RegistroHashExtensivel<ParChaveEndereco>`.

### **5. Classe HashExtensivel<T extends RegistroHashExtensivel<T>>**

- **Descrição**: Implementação da tabela hash extensível fornecida pelo professor, utilizada para indexação dos registros. Permite operações de inserção, busca, atualização e remoção de pares chave-endereço.
- **Principais Métodos**:
  - `boolean create(T elemento)`: Insere um novo par chave-endereço no índice.
  - `T read(int chave)`: Recupera o par chave-endereço associado à chave.
  - `boolean update(T elemento)`: Atualiza o endereço associado à chave.
  - `boolean delete(int chave)`: Remove o par chave-endereço do índice.

### **6. Classe Arquivo<T extends Registro & RegistroHashExtensivel<T>>**

- **Descrição**: Classe genérica responsável por gerenciar as operações CRUD em um arquivo binário, utilizando um índice direto implementado com a tabela hash extensível.
- **Atributos**:
  - `RandomAccessFile arquivo`: Arquivo binário que armazena os registros.
  - `Constructor<T> construtor`: Construtor da classe genérica T.
  - `int ultimoID`: Último ID utilizado.
  - `HashExtensivel<ParChaveEndereco> indice`: Instância da tabela hash extensível para indexação.
- **Principais Métodos**:
  - `Arquivo(Constructor<T> construtor, String nomeArquivo)`: Construtor que inicializa o arquivo e o índice.
  - `int create(T objeto)`: Insere um novo registro no arquivo e no índice, retornando o ID.
  - `T read(int id)`: Lê um registro a partir do ID, utilizando o índice.
  - `boolean update(T objeto)`: Atualiza um registro existente, tratando alterações no tamanho do registro e atualizando o índice se necessário.
  - `boolean delete(int id)`: Marca um registro como excluído e remove a entrada correspondente do índice.

### **7. Classe IO**

- **Descrição**: Classe que contém o método `main` e demonstra o uso das operações CRUD com a entidade Tarefa.
- **Funcionalidades**:
  - Criação de objetos `Tarefa` para testes.
  - Execução das operações de criação, leitura, atualização e exclusão.
  - Exibição dos resultados no console.

## **Relato da Experiência**

Durante o desenvolvimento deste trabalho, buscamos implementar todos os requisitos especificados, incluindo a integração da tabela hash extensível fornecida pelo professor como índice direto para otimizar as operações de busca.

**Implementação dos Requisitos**

- **Todos os requisitos foram implementados?**
  - Sim, implementamos todos os requisitos, incluindo o CRUD genérico e o índice direto com tabela hash extensível.

**Desafios Encontrados**

- **Operação Mais Difícil**
  - A integração da tabela hash extensível foi a operação mais desafiadora. Compreender o funcionamento interno da classe `HashExtensivel` e garantir que ela interagisse corretamente com a classe `Arquivo` exigiu muita atenção.

- **Desafios na Implementação**
  - Tivemos que ajustar a classe `Tarefa` para implementar a interface `RegistroHashExtensivel<Tarefa>`, adicionando métodos como `hashCode()` e `size()`.
  - Criamos a classe auxiliar `ParChaveEndereco` para representar o par chave-endereço necessário para o índice.
  - Adaptamos a classe `Arquivo` para trabalhar com o índice, atualizando os métodos CRUD para interagir com a `HashExtensivel`.

**Resultados Alcançados**

- As operações de criação, leitura, atualização e exclusão funcionam corretamente, com o índice sendo atualizado conforme necessário.
- A busca de registros é mais eficiente graças ao índice direto.
- Conseguimos manipular registros de tamanho variável, tratando corretamente aumentos e reduções no tamanho dos registros durante as atualizações.

## **Checklist**

- **O trabalho possui um índice direto implementado com a tabela hash extensível?**
  - **Resposta**: Sim

- **A operação de inclusão insere um novo registro no fim do arquivo e no índice e retorna o ID desse registro?**
  - **Resposta**: Sim

- **A operação de busca retorna os dados do registro, após localizá-lo por meio do índice direto?**
  - **Resposta**: Sim

- **A operação de alteração altera os dados do registro e trata corretamente as reduções e aumentos no espaço do registro?**
  - **Resposta**: Sim

- **A operação de exclusão marca o registro como excluído e o remove do índice direto?**
  - **Resposta**: Sim

- **O trabalho está funcionando corretamente?**
  - **Resposta**: Sim

- **O trabalho está completo?**
  - **Resposta**: Sim

- **O trabalho é original e não a cópia de um trabalho de outro grupo?**
  - **Resposta**: Sim

## **Considerações Finais**

A implementação deste trabalho nos proporcionou um aprendizado significativo sobre manipulação de arquivos binários, serialização de objetos e estruturas de dados avançadas como a tabela hash extensível. A integração do índice direto melhorou consideravelmente a eficiência das operações de busca.

Apesar dos desafios encontrados, conseguimos alcançar todos os objetivos propostos.

**Possíveis Melhorias**

- Implementar tratamento de exceções mais robusto, oferecendo feedback mais detalhado em caso de erros.
- Implementar uma interface gráfica para facilitar a interação do usuário com o sistema.

