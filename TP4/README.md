# **Trabalho Prático 4 - AED3**

### Grupo: Arthur Castro, Matheus Campbell e Pedro Gaioso

---

## **Descrição do Trabalho**
Este projeto implementa um sistema de backup e recuperação de arquivos de dados e índices do sistema de gerenciamento de tarefas. O objetivo é compactar todos os arquivos do sistema utilizando o algoritmo de compressão **LZW** e armazená-los em um único arquivo compactado para arquivamento. Além disso, o sistema permite a recuperação dos arquivos compactados, possibilitando ao usuário selecionar a versão de backup desejada.

### **Funcionalidades Principais**
1. **Compactação com LZW**:
   - Todos os arquivos de dados e índices são tratados como vetores de bytes.
   - Compactação sequencial usando um dicionário único de 12 bits para todo o conjunto de arquivos.

2. **Arquivamento Compactado**:
   - Cada arquivo armazenado no backup contém: 
     - Nome do arquivo.
     - Tamanho do vetor de bytes compactado.
     - Vetor de bytes compactado.

3. **Descompactação e Recuperação**:
   - Permite ao usuário escolher uma versão do backup para restaurar.
   - Os arquivos são descompactados e reconstituídos com seus conteúdos originais.

4. **Armazenamento Versionado**:
   - Os backups são organizados em pastas com nomes que identificam a versão ou data do backup.

5. **Eficiência no Fluxo de Dados**:
   - Leitura e compressão feitas em fluxo para evitar carregar todo o arquivo em memória.

---

## **Descrição das Classes e Métodos**



---

## **Relato da Experiência**
O desenvolvimento deste trabalho foi uma experiência técnica desafiadora e enriquecedora. Abaixo, destacamos os principais pontos:

1. **Requisitos Implementados**:
   - Todas as funcionalidades especificadas foram implementadas, incluindo compactação e descompactação com LZW, arquivamento versionado e recuperação.

2. **Desafios Enfrentados**:
   - A implementação do algoritmo LZW em um fluxo de bytes contínuo exigiu cuidado para evitar problemas de desempenho e de memória.
   - Garantir que a compactação e descompactação preservassem 100% dos dados originais foi desafiador, especialmente no teste com arquivos grandes.

3. **Resultados**:
   - O sistema alcançou uma taxa de compressão satisfatória, reduzindo o tamanho dos backups significativamente.
   - Todas as funcionalidades estão funcionando corretamente.

---

## **Checklist**
- **Há uma rotina de compactação usando o algoritmo LZW para fazer backup dos arquivos?**
  - Sim
- **Há uma rotina de descompactação usando o algoritmo LZW para recuperação dos arquivos?**
  - Sim
- **O usuário pode escolher a versão a recuperar?**
  - Sim
- **Qual foi a taxa de compressão alcançada por esse backup? (Compare o tamanho dos arquivos compactados com os arquivos originais)**
  - Taxa de compressão média:
- **O trabalho está funcionando corretamente?**
  - Sim
- **O trabalho está completo?**
  - Sim
- **O trabalho é original e não a cópia de um trabalho de um colega?**
  - Sim