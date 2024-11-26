# **Trabalho Prático 3 - AED3**

## **Integrantes do Grupo**
- **Nome do Participante 1**
- **Nome do Participante 2**
- (adicione outros integrantes, se houver)

---

## **Descrição do Trabalho**
Este projeto é uma implementação de um sistema de gerenciamento de tarefas desenvolvido para o terceiro trabalho prático da disciplina de AED3. O objetivo do projeto é implementar funcionalidades avançadas de organização e busca de tarefas, utilizando conceitos como:

1. **Índice Invertido**:
    - Usado para buscar tarefas por palavras na descrição, associando termos às tarefas que as contêm.
2. **Rótulos**:
    - Sistema de rótulos para organizar e buscar tarefas de maneira mais flexível, implementado com uma relação N:N entre tarefas e rótulos.
3. **Árvore B+**:
    - Gerencia os rótulos e o relacionamento N:N entre tarefas e rótulos.
4. **CRUD Completo**:
    - Operações de criação, leitura, atualização e exclusão para tarefas, categorias e rótulos.

O sistema permite:
- Criar, listar, atualizar e excluir tarefas, categorias e rótulos.
- Associar rótulos às tarefas.
- Buscar tarefas por palavras e por rótulos.
- Garantir eficiência nas operações de busca e relacionamento usando árvores B+ e índice invertido.

---

## **Descrição das Classes e Métodos**
### **Classe Arquivo**
- **Objetivo:** Gerenciar a persistência de registros genéricos, incluindo tarefas, categorias e rótulos.
- **Métodos principais:**
    - `create(T objeto)`: Cria um novo registro no arquivo e atualiza o índice.
    - `read(int id)`: Recupera um registro pelo ID usando o índice.
    - `update(T objeto)`: Atualiza um registro existente, alocando espaço adicional se necessário.
    - `delete(int id)`: Exclui logicamente um registro.
    - `readAll()`: Lista todos os registros não excluídos.

### **Classe ListaInvertida**
- **Objetivo:** Implementa o índice invertido para associar termos às tarefas.
- **Métodos principais:**
    - `create(String chave, ElementoLista elemento)`: Adiciona um termo e o ID da tarefa associada ao índice.
    - `read(String chave)`: Retorna os IDs de tarefas associados a um termo.
    - `delete(String chave, int id)`: Remove um ID de tarefa associado a um termo.

### **Classe Rotulo**
- **Objetivo:** Representa os rótulos que podem ser associados às tarefas.
- **Atributos principais:**
    - `id`: Identificador único do rótulo.
    - `rotulo`: Nome ou descrição do rótulo.
- **Métodos principais:**
    - `toByteArray()` / `fromByteArray()`: Serialização e desserialização do registro.

### **Classe ArquivoRotulos**
- **Objetivo:** Gerenciar o CRUD de rótulos.
- **Métodos principais:**
    - `create(Rotulo rotulo)`: Adiciona um novo rótulo.
    - `update(Rotulo rotulo)`: Atualiza os dados de um rótulo.
    - `delete(int id)`: Exclui um rótulo.
    - `listarRotulos()`: Lista todos os rótulos cadastrados.

### **Classe ArquivoTarefas**
- **Objetivo:** Gerenciar o CRUD de tarefas e o relacionamento com rótulos.
- **Métodos principais:**
    - `vincularRotulo(int idTarefa, int idRotulo)`: Vincula um rótulo a uma tarefa.
    - `desvincularRotulo(int idTarefa, int idRotulo)`: Remove a associação entre uma tarefa e um rótulo.
    - `buscarPorTermos(String consulta)`: Busca tarefas usando o índice invertido.
    - `listarTarefasPorRotulo(int idRotulo)`: Lista tarefas associadas a um rótulo.

---

## **Relato da Experiência**
O desenvolvimento deste trabalho foi desafiador e enriquecedor. Aqui estão alguns dos pontos principais:

1. **Requisitos Implementados**:
    - Implementamos todos os requisitos especificados no enunciado, incluindo o CRUD de rótulos, busca por palavras e busca por rótulos.

2. **Desafios Enfrentados**:
    - A integração entre o índice invertido e a busca por tarefas foi a parte mais desafiadora, devido à necessidade de garantir eficiência.
    - Implementar a árvore B+ para gerenciar o relacionamento N:N entre tarefas e rótulos exigiu atenção aos detalhes e testes rigorosos.

3. **Resultados**:
    - O sistema está funcionando conforme o esperado, com todas as operações de CRUD e buscas implementadas. Os resultados foram satisfatórios.

---

## **Checklist**
- **O índice invertido com os termos das tarefas foi criado usando a classe ListaInvertida?**
    - Sim
- **O CRUD de rótulos foi implementado?**
    - Sim
- **No arquivo de tarefas, os rótulos são incluídos, alterados e excluídos em uma árvore B+?**
    - Sim
- **É possível buscar tarefas por palavras usando o índice invertido?**
    - Sim
- **É possível buscar tarefas por rótulos usando uma árvore B+?**
    - Sim
- **O trabalho está completo?**
    - Sim
- **O trabalho é original e não a cópia de um trabalho de um colega?**
    - Sim
