import aed3.ArquivoCategorias;
import aed3.ArquivoTarefas;
import aed3.Categoria;
import aed3.Tarefa;

public class IO {

    public static void main(String[] args) {
        try {
            ArquivoTarefas arqTarefas = new ArquivoTarefas(Tarefa.class.getConstructor(), "tarefas.db");
            ArquivoCategorias arqCategorias = new ArquivoCategorias(Categoria.class.getConstructor(), "categorias.db");

            // Cria categorias
            Categoria c1 = new Categoria(-1, "Trabalho");
            Categoria c2 = new Categoria(-1, "Pessoal");
            arqCategorias.create(c1);
            arqCategorias.create(c2);

            // Cria várias tarefas para a categoria Trabalho
            Tarefa t1 = new Tarefa(-1, c1.getID(), "Estudar Java", "2023-01-01", "", "Pendente", "Alta");
            Tarefa t2 = new Tarefa(-1, c1.getID(), "Fazer documentação", "2023-01-02", "", "Em Progresso", "Média");
            Tarefa t3 = new Tarefa(-1, c1.getID(), "Reunião com equipe", "2023-01-03", "", "Pendente", "Alta");

            // Cria uma tarefa para a categoria Pessoal
            Tarefa t4 = new Tarefa(-1, c2.getID(), "Ir ao supermercado", "2023-01-02", "", "Pendente", "Média");

            // Adiciona as tarefas
            arqTarefas.create(t1);
            arqTarefas.create(t2);
            arqTarefas.create(t3);
            arqTarefas.create(t4);

            // Listar todas as tarefas da categoria Trabalho (mais de uma tarefa)
            System.out.println("Tarefas na categoria Trabalho:");
            arqTarefas.listarTarefasPorCategoria(c1.getID());

            // Listar todas as tarefas da categoria Pessoal
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
            System.out.println("\nExcluindo a tarefa 4 (Pessoal)...");
            arqTarefas.delete(t4.getID());

            // Tentar listar tarefa excluída
            Tarefa tarefaExcluida = arqTarefas.read(t4.getID());
            if (tarefaExcluida == null) {
                System.out.println("Tarefa 4 foi excluída com sucesso.");
            } else {
                System.out.println("Falha ao excluir a tarefa 4.");
            }

            // Tentativa de exclusão de categoria com tarefas vinculadas
            System.out.println("\nTentativa de exclusão da categoria 'Trabalho' com tarefas vinculadas...");
            boolean exclusaoCategoria = arqCategorias.deleteCategoria(c1.getID(), arqTarefas);
            if (!exclusaoCategoria) {
                System.out.println("Categoria 'Trabalho' não pôde ser excluída, pois há tarefas vinculadas.");
            }

            // Excluir todas as tarefas da categoria Trabalho
            System.out.println("\nExcluindo as tarefas da categoria 'Trabalho'...");
            arqTarefas.delete(t1.getID());
            arqTarefas.delete(t2.getID());
            arqTarefas.delete(t3.getID());

            // Agora, tentar excluir a categoria 'Trabalho' após remover todas as tarefas
            System.out.println("\nTentativa de exclusão da categoria 'Trabalho' após excluir as tarefas...");
            exclusaoCategoria = arqCategorias.deleteCategoria(c1.getID(), arqTarefas);
            if (exclusaoCategoria) {
                System.out.println("Categoria 'Trabalho' foi excluída com sucesso.");
            } else {
                System.out.println("Falha ao excluir a categoria 'Trabalho'.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
