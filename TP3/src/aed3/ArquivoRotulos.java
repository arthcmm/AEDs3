//todos os erros (search, remove, listAllKeys são de falta do método na classe BPlusTree)
package aed3;

import java.lang.reflect.Constructor;

public class ArquivoRotulos extends Arquivo<Rotulo> {
    private final BPlusTree<String, Integer> indicePorRotulo; // Índice indireto para buscar pela descrição do rótulo

    public ArquivoRotulos(Constructor<Rotulo> construtor, String nomeArquivo) throws Exception {
        super(construtor, nomeArquivo);
        indicePorRotulo = new BPlusTree<>(4); // Inicializa a Árvore B+ com ordem 4
    }

    @Override
    public int create(Rotulo rotulo) throws Exception {
        int id = super.create(rotulo);
        indicePorRotulo.insert(rotulo.getRotulo(), id); // Insere no índice pela descrição
        return id;
    }

    @Override
    public boolean update(Rotulo rotulo) throws Exception {
        Rotulo antigo = super.read(rotulo.getId());
        if (antigo != null) {
            // Atualiza o índice pela descrição
            indicePorRotulo.remove(antigo.getRotulo());
            indicePorRotulo.insert(rotulo.getRotulo(), rotulo.getId());
        }
        return super.update(rotulo);
    }

    @Override
    public boolean delete(int id) throws Exception {
        Rotulo rotulo = super.read(id);
        if (rotulo != null) {
            indicePorRotulo.remove(rotulo.getRotulo()); // Remove do índice pela descrição
        }
        return super.delete(id);
    }

    public String listarRotulos() throws Exception {
        StringBuilder sb = new StringBuilder();
        for (String descricao : indicePorRotulo.listAllKeys()) {
            int id = indicePorRotulo.search(descricao);
            Rotulo rotulo = super.read(id);
            if (rotulo != null) {
                sb.append(rotulo).append("\n");
            }
        }
        return sb.toString();
    }

    public Rotulo buscarPorRotulo(String rotulo) throws Exception {
        Integer id = indicePorRotulo.search(rotulo);
        return (id != null) ? super.read(id) : null;
    }

    public boolean existeRotuloAssociado(int idRotulo, ArquivoTarefas arqTarefas) throws Exception {
        return arqTarefas.existemTarefasComRotulo(idRotulo);
    }
}
