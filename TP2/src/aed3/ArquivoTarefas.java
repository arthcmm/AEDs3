package aed3;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class ArquivoTarefas extends Arquivo<Tarefa> {
    private final BPlusTree<Integer, List<Integer>> arvoreCategoriaTarefa;

    public ArquivoTarefas(Constructor<Tarefa> construtor, String nomeArquivo) throws Exception {
        super(construtor, nomeArquivo);

        // Inicializa a árvore B+ para manter o relacionamento 1:N entre categoria e tarefas
        arvoreCategoriaTarefa = new BPlusTree<>(4);
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

        // Insere ou atualiza a árvore B+ com a lista de tarefas atualizada para a categoria
        arvoreCategoriaTarefa.insert(tarefa.getIdCategoria(), tarefasDaCategoria);

        return id;
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
}
