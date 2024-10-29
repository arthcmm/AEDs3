import aed3.ArquivoCategorias;
import aed3.ArquivoTarefas;
import aed3.Categoria;
import aed3.Tarefa;

public class IO {
    private static ArquivoTarefas arqTarefas;
    private static ArquivoCategorias arqCategorias;

    public static void main(String[] args) {
        try {
            arqTarefas = new ArquivoTarefas(Tarefa.class.getConstructor(), "tarefas.db");
            arqCategorias = new ArquivoCategorias(Categoria.class.getConstructor(), "categorias.db");

            // Cria categorias
            Categoria c1 = new Categoria(-1, "Trabalho");
            Categoria c2 = new Categoria(-1, "Pessoal");
            arqCategorias.create(c1);
            arqCategorias.create(c2);

            // Cria tarefas
            Tarefa t1 = new Tarefa(-1, c1.getID(), "Estudar Java", "2023-01-01", "", "Pendente", "Alta");
            Tarefa t2 = new Tarefa(-1, c2.getID(), "Ir ao supermercado", "2023-01-02", "", "Pendente", "Média");
            arqTarefas.create(t1);
            arqTarefas.create(t2);

            // Listar tarefas por categoria
            System.out.println("Tarefas na categoria Trabalho:");
            arqTarefas.listarTarefasPorCategoria(c1.getID());

            System.out.println("\nTarefas na categoria Pessoal:");
            arqTarefas.listarTarefasPorCategoria(c2.getID());

            // Atualizar tarefa
            System.out.println("\nAtualizando tarefa...");
            t1.setStatus("Concluída");
            t1.setDataConclusao("2023-01-10");
            arqTarefas.update(t1);

            // Listar tarefas após a atualização
            System.out.println("\nTarefas na categoria Trabalho após a atualização:");
            arqTarefas.listarTarefasPorCategoria(c1.getID());

            // Excluir tarefa
            System.out.println("\nExcluindo a tarefa 2...");
            arqTarefas.delete(t2.getID());

            // Tentar listar tarefa excluída
            Tarefa tarefaExcluida = arqTarefas.read(t2.getID());
            if (tarefaExcluida == null) {
                System.out.println("Tarefa 2 foi excluída com sucesso.");
            } else {
                System.out.println("Falha ao excluir a tarefa 2.");
            }

            // Tentar excluir uma categoria com tarefas vinculadas
            System.out.println("\nTentativa de exclusão da categoria 'Trabalho' com tarefas vinculadas...");
            arqCategorias.deleteCategoria(c1.getID(), arqTarefas);

            // Excluir a tarefa da categoria Trabalho
            System.out.println("\nExcluindo a tarefa da categoria 'Trabalho'...");
            arqTarefas.delete(t1.getID());

            // Agora, tentar excluir a categoria 'Trabalho' após remover a tarefa
            System.out.println("\nTentativa de exclusão da categoria 'Trabalho' após excluir as tarefas...");
            arqCategorias.deleteCategoria(c1.getID(), arqTarefas);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
