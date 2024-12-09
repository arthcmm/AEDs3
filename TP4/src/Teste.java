import aed3.*;
import java.time.LocalDate;
import java.util.List;

public class Teste {

    public static void main(String[] args) {
        try {
            // Instanciando os arquivos de tarefas, categorias e rótulos
            ArquivoTarefas arqTarefas = new ArquivoTarefas(Tarefa.class.getConstructor(), "tarefas.db");
            ArquivoCategorias arqCategorias = new ArquivoCategorias(Categoria.class.getConstructor(), "categorias.db");
            ArquivoRotulos arqRotulos = new ArquivoRotulos(Rotulo.class.getConstructor(), "rotulos.db");

            // Criar categorias e verificar
            System.out.println("Criando categorias...");
            Categoria c1 = new Categoria(-1, "Trabalho");
            Categoria c2 = new Categoria(-1, "Pessoal");
            c1.setID(arqCategorias.create(c1));
            c2.setID(arqCategorias.create(c2));
            System.out.println("Categorias criadas: ");
            arqCategorias.listarCategorias();

            // Criar rótulos e verificar
            System.out.println("\nCriando rótulos...");
            Rotulo r1 = new Rotulo(-1, "Urgente");
            Rotulo r2 = new Rotulo(-1, "Importante");
            Rotulo r3 = new Rotulo(-1, "Baixa prioridade");
            r1.setID(arqRotulos.create(r1));
            r2.setID(arqRotulos.create(r2));
            r3.setID(arqRotulos.create(r3));
            System.out.println("Rótulos criados: " + arqRotulos.listarRotulos());

            // Criar tarefas e verificar
            System.out.println("\nCriando tarefas...");
            Tarefa t1 = new Tarefa(-1, c1.getID(), "Estudar Java", LocalDate.parse("2023-01-01"), null, "Pendente", 1);
            Tarefa t2 = new Tarefa(-1, c1.getID(), "Fazer documentação", LocalDate.parse("2023-01-02"), null, "Em Progresso", 2);
            Tarefa t3 = new Tarefa(-1, c1.getID(), "Reunião com equipe", LocalDate.parse("2023-01-03"), null, "Pendente", 2);
            Tarefa t4 = new Tarefa(-1, c2.getID(), "Ir ao supermercado", LocalDate.parse("2023-01-02"), null, "Pendente", 1);
            Tarefa t5 = new Tarefa(-1, c2.getID(), "teste de persistencia", LocalDate.parse("2023-01-02"), null, "Pendente", 1);
            t1.setID(arqTarefas.create(t1));
            t2.setID(arqTarefas.create(t2));
            t3.setID(arqTarefas.create(t3));
            t4.setID(arqTarefas.create(t4));
            t5.setID(arqTarefas.create(t5));
            System.out.println("Tarefas criadas:");
            arqTarefas.listarTodasAsTarefas();

            // Vincular rótulos às tarefas e verificar
            System.out.println("\nVinculando rótulos às tarefas...");
            arqTarefas.vincularRotulo(t1.getID(), r1.getID());
            arqTarefas.vincularRotulo(t1.getID(), r2.getID());
            arqTarefas.vincularRotulo(t2.getID(), r2.getID());
            arqTarefas.vincularRotulo(t3.getID(), r3.getID());
            System.out.println("Rótulos vinculados com sucesso.");

            // Listar tarefas por categoria
            System.out.println("\nTarefas na categoria 'Trabalho':");
            arqTarefas.listarTarefasPorCategoria(c1.getID());

            // Listar tarefas por rótulo
            System.out.println("\nTarefas com o rótulo 'Urgente':");
            List<Tarefa> tarefasUrgentes = arqTarefas.listarTarefasPorRotulo(r1.getID());
            for (Tarefa tarefa : tarefasUrgentes) {
                System.out.println(tarefa);
            }

            // Busca por palavras
            System.out.println("\nBuscando tarefas com a palavra 'Java':");
            List<Tarefa> tarefasJava = arqTarefas.buscarPorTermos("Java");
            for (Tarefa tarefa : tarefasJava) {
                System.out.println(tarefa);
            }

            // Atualizar tarefa
            System.out.println("\nAtualizando tarefa...");
            t1.setStatus("Concluída");
            t1.setDataConclusao(LocalDate.parse("2023-01-10"));
            arqTarefas.update(t1);
            System.out.println("Tarefa atualizada:");
            arqTarefas.listarTarefasPorCategoria(c1.getID());

            // Excluir tarefa e verificar
            System.out.println("\nExcluindo tarefa 4 (Pessoal)...");
            arqTarefas.delete(t4.getID());
            Tarefa tarefaExcluida = arqTarefas.read(t4.getID());
            if (tarefaExcluida == null) {
                System.out.println("Tarefa 4 excluída com sucesso.");
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
            System.out.println("\nExcluindo todas as tarefas da categoria 'Trabalho'...");
            arqTarefas.delete(t1.getID());
            arqTarefas.delete(t2.getID());
            arqTarefas.delete(t3.getID());

            // Excluir a categoria após remoção das tarefas
            System.out.println("\nTentativa de exclusão da categoria 'Trabalho' após excluir todas as tarefas...");
            exclusaoCategoria = arqCategorias.deleteCategoria(c1.getID(), arqTarefas);
            if (exclusaoCategoria) {
                System.out.println("Categoria 'Trabalho' foi excluída com sucesso.");
            } else {
                System.out.println("Falha ao excluir a categoria 'Trabalho'.");
            }

            // Verificar categorias restantes
            System.out.println("\nCategorias restantes:");
            arqCategorias.listarCategorias();

            // **Listar todas as tarefas restantes**
            System.out.println("\nListando todas as tarefas restantes no sistema:");
            arqTarefas.listarTodasAsTarefas();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
