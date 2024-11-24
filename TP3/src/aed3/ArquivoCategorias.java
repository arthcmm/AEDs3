package aed3;

import java.lang.reflect.Constructor;

public class ArquivoCategorias extends Arquivo<Categoria> {
    private final BPlusTree<String, Integer> indicePorNome; // Índice indireto para buscar pelo nome da categoria

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

    public boolean deleteCategoria(int idCategoria, ArquivoTarefas arqTarefas) throws Exception {
        if (arqTarefas.existemTarefasNaCategoria(idCategoria)) {
            System.out.println("Categoria não pode ser excluída, pois há tarefas vinculadas.");
            return false;
        }
        return super.delete(idCategoria);
    }      

}
