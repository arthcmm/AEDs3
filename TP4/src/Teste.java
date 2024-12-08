import aed3.*;
import java.time.LocalDate;

public class Teste {

    public static void main(String[] args) {
        try {
            // Instanciando os arquivos de tarefas, categorias e rótulos
            ArquivoTarefas arqTarefas = new ArquivoTarefas(Tarefa.class.getConstructor(), "tarefas.db");
            ArquivoCategorias arqCategorias = new ArquivoCategorias(Categoria.class.getConstructor(), "categorias.db");
            ArquivoRotulos arqRotulos = new ArquivoRotulos(Rotulo.class.getConstructor(), "rotulos.db");

            // Cria categorias
            Categoria c1 = new Categoria(-1, "Trabalho");
            Categoria c2 = new Categoria(-1, "Pessoal");
            arqCategorias.create(c1);
            arqCategorias.create(c2);

            // Cria rótulos
            Rotulo r1 = new Rotulo(-1, "Urgente");
            Rotulo r2 = new Rotulo(-1, "Importante");
            Rotulo r3 = new Rotulo(-1, "Baixa prioridade");
            arqRotulos.create(r1);
            arqRotulos.create(r2);
            arqRotulos.create(r3);

            // Cria várias tarefas para a categoria Trabalho
            Tarefa t1 = new Tarefa(-1, c1.getID(), "Estudar Java", LocalDate.parse("2023-01-01"), null, "Pendente", 1);
            Tarefa t2 = new Tarefa(-1, c1.getID(), "Fazer documentação", LocalDate.parse("2023-01-02"), null, "Em Progresso", 2);
            Tarefa t3 = new Tarefa(-1, c1.getID(), "Reunião com equipe", LocalDate.parse("2023-01-03"), null, "Pendente", 2);

            // Cria uma tarefa para a categoria Pessoal
            Tarefa t4 = new Tarefa(-1, c2.getID(), "Ir ao supermercado", LocalDate.parse("2023-01-02"), null, "Pendente", 1);

            // Adiciona as tarefas
            arqTarefas.create(t1);
            arqTarefas.create(t2);
            arqTarefas.create(t3);
            arqTarefas.create(t4);

            // Vincula rótulos às tarefas
            arqTarefas.vincularRotulo(t1.getID(), r1.getID());
            arqTarefas.vincularRotulo(t1.getID(), r2.getID());
            arqTarefas.vincularRotulo(t2.getID(), r2.getID());
            arqTarefas.vincularRotulo(t3.getID(), r3.getID());

            // Listar todas as tarefas
            System.out.println("Listando todas as tarefas:");
            arqTarefas.listarTodasAsTarefas();

            // Listar categorias
            System.out.println("\nListando todas as categorias:");
            arqCategorias.listarCategorias();

            // Listar rótulos
            System.out.println("\nListando todos os rótulos:");
            System.out.println(arqRotulos.listarRotulos());

            // Listar tarefas por categoria
            System.out.println("\nTarefas na categoria Trabalho:");
            arqTarefas.listarTarefasPorCategoria(c1.getID());

            // Listar tarefas por rótulo
            System.out.println("\nTarefas com o rótulo 'Urgente':");
            for (Tarefa tarefa : arqTarefas.listarTarefasPorRotulo(r1.getID())) {
                System.out.println(tarefa);
            }

            // Busca por palavras usando índice invertido
            System.out.println("\nBuscando tarefas com a palavra 'Java':");
            for (Tarefa tarefa : arqTarefas.buscarPorTermos("Java")) {
                System.out.println(tarefa);
            }

            // Atualizar tarefa
            System.out.println("\nAtualizando tarefa...");
            t1.setStatus("Concluída");
            t1.setDataConclusao(LocalDate.parse("2023-01-10"));
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

            arqCategorias.listarCategorias();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
