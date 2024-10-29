package aed3;

import java.lang.reflect.Constructor;
import java.util.List;

public class ArquivoTarefas extends Arquivo<Tarefa> {
    private BPlusTree<Integer, Integer> arvoreCategoriaTarefa; // B+ Tree para armazenar (idCategoria, idTarefa)

    public ArquivoTarefas(Constructor<Tarefa> construtor, String nomeArquivo) throws Exception {
        super(construtor, nomeArquivo);
        // Inicializar a árvore B+ para manter o relacionamento 1:N entre categoria e tarefas
        arvoreCategoriaTarefa = new BPlusTree<>(4); // Grau da árvore B+ = 4 (pode ajustar conforme necessário)
    }

    @Override
    public int create(Tarefa tarefa) throws Exception {
        int id = super.create(tarefa);
        arvoreCategoriaTarefa.insert(tarefa.getIdCategoria(), id); // Insere na árvore B+
        return id;
    }

    @Override
    public boolean delete(int id) throws Exception {
        Tarefa tarefa = super.read(id);
        if (tarefa != null) {
            arvoreCategoriaTarefa.remove(tarefa.getIdCategoria(), id); // Remove o par da árvore B+
        }
        return super.delete(id);
    }

    public void listarTarefasPorCategoria(int idCategoria) throws Exception {
        System.out.println("Tarefas na categoria " + idCategoria + ":");
        for (Integer idTarefa : arvoreCategoriaTarefa.get(idCategoria)) {
            System.out.println(super.read(idTarefa));
        }
    }

    public boolean existemTarefasNaCategoria(int idCategoria) {
        List<Integer> tarefas = arvoreCategoriaTarefa.get(idCategoria);
        return tarefas != null && !tarefas.isEmpty(); // Verifica se a lista de tarefas não está vazia
    }
}
