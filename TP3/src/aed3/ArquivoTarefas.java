package aed3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.*;


public class ArquivoTarefas extends Arquivo<Tarefa> {
    private final BPlusTree<Integer, List<Integer>> arvoreCategoriaTarefa;

    private Set<String> stopWords;
    private ListaInvertida indiceInvertido;
    private ListaInvertida relacaoTarefaRotulo;
    private ListaInvertida relacaoRotuloTarefa;
    public ArquivoTarefas(Constructor<Tarefa> construtor, String nomeArquivo) throws Exception {
        super(construtor, nomeArquivo);

        // Inicializa a árvore B+ para manter o relacionamento 1:N entre categoria e tarefas
        arvoreCategoriaTarefa = new BPlusTree<>(4);
        indiceInvertido = new ListaInvertida(4, super.filePath+"indiceInvertido.dic", super.filePath+"indiceInvertido.blk");
        relacaoTarefaRotulo = new ListaInvertida(4, super.filePath+"relTarefaRotulo.dic", super.filePath+"relTarefaRotulo.blk");
        relacaoRotuloTarefa = new ListaInvertida(4, super.filePath+"relRotuloTarefa.dic", super.filePath+"relRotuloTarefa.blk");
        carregarStopWords(super.filePath+"stopwords.txt");
    }

    @Override
    public int create(Tarefa tarefa) throws Exception {
        int id = super.create(tarefa); // Chama o método create original

        // Obter a lista de tarefas da categoria
        List<Integer> tarefasDaCategoria = arvoreCategoriaTarefa.get(tarefa.getIdCategoria());

        // Se não houver nenhuma tarefa associada à categoria, cria uma nova lista
        if (tarefasDaCategoria == null) {
            tarefasDaCategoria = new ArrayList<>(); // Cria uma nova lista para as tarefas
        }

        // Adiciona o ID da nova tarefa à lista de tarefas da categoria
        tarefasDaCategoria.add(id);

        // Realiza a indexação da descrição
        indexarDescricao(id, tarefa.getNome());

        // Insere ou atualiza a árvore B+ com a lista de tarefas atualizada para a categoria
        arvoreCategoriaTarefa.insert(tarefa.getIdCategoria(), tarefasDaCategoria);

        return id;
    }


    @Override
    public boolean update(Tarefa tarefa) throws Exception {
        Tarefa antiga = super.read(tarefa.getID());
        if (antiga != null) {
            desindexarDescricao(antiga.getID(), antiga.getNome());
        }
        boolean atualizado = super.update(tarefa);
        if (atualizado) {
            indexarDescricao(tarefa.getID(), tarefa.getNome());
        }
        return atualizado;
    }


    @Override
    public boolean delete(int id) throws Exception {
        Tarefa tarefa = super.read(id);
        if (tarefa != null) {
            // Remove o ID da tarefa da lista de tarefas da categoria na árvore B+
            List<Integer> tarefasDaCategoria = arvoreCategoriaTarefa.get(tarefa.getIdCategoria());
            if (tarefasDaCategoria != null) {
                tarefasDaCategoria.remove(Integer.valueOf(id));
                if (tarefasDaCategoria.isEmpty()) {
                    arvoreCategoriaTarefa.remove(tarefa.getIdCategoria(), tarefasDaCategoria);
                } else {
                    arvoreCategoriaTarefa.insert(tarefa.getIdCategoria(), tarefasDaCategoria); // Atualiza a árvore
                }
            }
        }
        return super.delete(id);
    }

    public void listarTodasAsTarefas() throws Exception {
        System.out.println("Lista de todas as tarefas:");
        for ( Tarefa tarefa : super.readAll()){
            System.out.println(tarefa);
        }
    }

    public void listarTarefasPorCategoria(int idCategoria) throws Exception {
        List<Integer> tarefasDaCategoria = arvoreCategoriaTarefa.get(idCategoria);
        if (tarefasDaCategoria == null || tarefasDaCategoria.isEmpty()) {
            System.out.println("Não há tarefas nesta categoria.");
            return;
        }

        System.out.println("Tarefas na categoria " + idCategoria + ":");
        for (Integer idTarefa : tarefasDaCategoria) {
            Tarefa tarefa = super.read(idTarefa);
            if (tarefa != null) {
                System.out.println(tarefa);
            }
        }
    }

    public boolean existemTarefasNaCategoria(int idCategoria) {
        List<Integer> tarefas = arvoreCategoriaTarefa.get(idCategoria);
        return tarefas != null && !tarefas.isEmpty(); // Verifica se a lista de tarefas não está vazia
    }

    private void indexarDescricao(int id, String descricao) throws Exception {
        String[] termos = descricao.toLowerCase().replaceAll("[^a-z0-9 ]", "").split("\\s+");
        for (String termo : termos) {
            if (!isStopWord(termo)) {
                indiceInvertido.create(termo, new ElementoLista(id, 1.0f));
            }
        }
    }

    private void desindexarDescricao(int id, String descricao) throws Exception {
        String[] termos = descricao.toLowerCase().replaceAll("[^a-z0-9 ]", "").split("\\s+");
        for (String termo : termos) {
            if (!isStopWord(termo)) {
                indiceInvertido.delete(termo, id);
            }
        }
    }


    private void carregarStopWords(String caminhoArquivo) throws IOException {
        stopWords = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                stopWords.add(linha.trim().toLowerCase()); // Adiciona cada linha como uma stop word
            }
        }
    }

    private boolean isStopWord(String termo) {
        return stopWords.contains(termo.toLowerCase());
    }

    public List<Tarefa> buscarPorTermos(String consulta) throws Exception {
        String[] termos = consulta.toLowerCase().replaceAll("[^a-z0-9 ]", "").split("\\s+");
        Map<Integer, Float> relevancias = new HashMap<>();

        for (String termo : termos) {
            ElementoLista[] resultados = indiceInvertido.read(termo);
            for (ElementoLista resultado : resultados) {
                relevancias.put(resultado.getId(),
                        relevancias.getOrDefault(resultado.getId(), 0.0f) + resultado.getFrequencia());
            }
        }

        return relevancias.entrySet().stream()
                .sorted(Map.Entry.<Integer, Float>comparingByValue().reversed())
                .map(entry -> {
                    try {
                        return super.read(entry.getKey());
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();
    }


    public void vincularRotulo(int idTarefa, int idRotulo) throws Exception {
        relacaoTarefaRotulo.create(String.valueOf(idTarefa), new ElementoLista(idRotulo, 1.0f));
        relacaoRotuloTarefa.create(String.valueOf(idRotulo), new ElementoLista(idTarefa, 1.0f));
    }

    public void desvincularRotulo(int idTarefa, int idRotulo) throws Exception {
        relacaoTarefaRotulo.delete(String.valueOf(idTarefa), idRotulo);
        relacaoRotuloTarefa.delete(String.valueOf(idRotulo), idTarefa);
    }

    public List<Rotulo> listarRotulosPorTarefa(int idTarefa, ArquivoRotulos arquivoRotulos) throws Exception {
        ElementoLista[] resultados = relacaoTarefaRotulo.read(String.valueOf(idTarefa));
        List<Rotulo> rotulos = new ArrayList<>();
        for (ElementoLista resultado : resultados) {
            rotulos.add(arquivoRotulos.read(resultado.getId()));
        }
        return rotulos;
    }

    public List<Tarefa> listarTarefasPorRotulo(int idRotulo) throws Exception {
        ElementoLista[] resultados = relacaoRotuloTarefa.read(String.valueOf(idRotulo));
        List<Tarefa> tarefas = new ArrayList<>();
        for (ElementoLista resultado : resultados) {
            tarefas.add(super.read(resultado.getId()));
        }
        return tarefas;
    }


    public boolean existemTarefasComRotulo(int idRotulo) throws Exception {
        return !listarTarefasPorRotulo(idRotulo).isEmpty();
    }
}
