package aed3;

import java.lang.reflect.Constructor;
import java.util.List;

public class ArquivoCategorias extends Arquivo<Categoria> {
    private BPlusTree<String, Integer> indicePorNome; // Índice indireto para buscar pelo nome da categoria

    public ArquivoCategorias(Constructor<Categoria> construtor, String nomeArquivo) throws Exception {
        super(construtor, nomeArquivo);
        indicePorNome = new BPlusTree<>(4); // Inicializa o índice de nomes (Árvore B+)
    }

    @Override
    public int create(Categoria categoria) throws Exception {
        int id = super.create(categoria);
        indicePorNome.insert(categoria.getNome(), id); // Insere no índice indireto
        return id;
    }

    public Categoria buscarPorNome(String nome) throws Exception {
        List<Integer> ids = indicePorNome.get(nome); // Retorna a lista de IDs associados ao nome
        if (ids != null && !ids.isEmpty()) {
            Integer id = ids.getFirst(); // Pega o primeiro ID da lista, supondo que o nome é único
            return super.read(id);
        }
        return null; // Se a lista estiver vazia ou for nula, retorna null
    }

    public void deleteCategoria(int idCategoria, ArquivoTarefas arqTarefas) throws Exception {
        if (arqTarefas.existemTarefasNaCategoria(idCategoria)) {
            System.out.println("Categoria não pode ser excluída, pois há tarefas vinculadas.");
            return;
        }
        super.delete(idCategoria);
    }

}
