import aed3.ArquivoCategorias;
import aed3.ArquivoTarefas;
import aed3.Categoria;
import aed3.Tarefa;

import java.time.LocalDate;

public class IO {

    public static void main(String[] args) {
        try {
            ArquivoTarefas arqTarefas = new ArquivoTarefas(Tarefa.class.getConstructor(), "tarefas.db");
            ArquivoCategorias arqCategorias = new ArquivoCategorias(Categoria.class.getConstructor(), "categorias.db");
            Scanner scanner = new Scanner(System.in);
            int resposta;

            while (true) {
                System.out.println("> Menu Principal");
                System.out.println("1) Gerenciar Tarefas");
                System.out.println("2) Gerenciar Categorias");
                System.out.println("3) Sair");
                resposta = scanner.nextInt();
                scanner.nextLine(); 

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

            //listar Todas As Tarefas
            arqTarefas.listarTodasAsTarefas();
            //listar categorias
            arqCategorias.listarCategorias();
            // Listar todas as tarefas da categoria Trabalho (mais de uma tarefa)
            System.out.println("Tarefas na categoria Trabalho:");
            arqTarefas.listarTarefasPorCategoria(c1.getID());

            // Listar todas as tarefas da categoria Pessoal
            System.out.println("\nTarefas na categoria Pessoal:");
            arqTarefas.listarTarefasPorCategoria(c2.getID());

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

    public static void gerenciarTarefas(ArquivoTarefas arqTarefas, ArquivoCategorias arqCategorias, Scanner scanner) throws Exception {
        int opcao;
        System.out.println("> Gerenciar Tarefas");
        System.out.println("1) Criar Tarefa");
        System.out.println("2) Listar Tarefas por Categoria");
        System.out.println("3) Atualizar Tarefa");
        System.out.println("4) Excluir Tarefa");
        System.out.println("5) Voltar");
        opcao = scanner.nextInt();
        scanner.nextLine();

        switch (opcao) {
            case 1: // Criar Tarefa
                System.out.print("Nome da Tarefa: ");
                String nome = scanner.nextLine();
                System.out.print("Data (YYYY-MM-DD): ");
                String data = scanner.nextLine();
                System.out.print("Status: ");
                String status = scanner.nextLine();
                System.out.print("Prioridade: ");
                String prioridade = scanner.nextLine();

                System.out.println("Escolha uma categoria:");
                // arqCategorias.listarCategorias();
                System.out.print("ID da Categoria: ");
                int categoriaID = scanner.nextInt();
                scanner.nextLine();

                Tarefa tarefa = new Tarefa(-1, categoriaID, nome, data, "", status, prioridade);
                arqTarefas.create(tarefa);
                System.out.println("Tarefa criada com sucesso!");
                break;
            case 2: // Listar Tarefas
                System.out.print("ID da Categoria para listar tarefas: ");
                int catID = scanner.nextInt();
                arqTarefas.listarTarefasPorCategoria(catID);
                break;
            case 3: // Atualizar Tarefa
                System.out.print("ID da Tarefa para atualizar: ");
                int tarefaID = scanner.nextInt();
                scanner.nextLine();

                Tarefa tarefaExistente = arqTarefas.read(tarefaID);
                if (tarefaExistente != null) {
                    System.out.print("Novo Status: ");
                    String novoStatus = scanner.nextLine();
                    tarefaExistente.setStatus(novoStatus);
                    arqTarefas.update(tarefaExistente);
                    System.out.println("Tarefa atualizada com sucesso!");
                } else {
                    System.out.println("Tarefa não encontrada.");
                }
                break;
            case 4: // Excluir Tarefa
                System.out.print("ID da Tarefa para excluir: ");
                int excluirID = scanner.nextInt();
                arqTarefas.delete(excluirID);
                System.out.println("Tarefa excluída com sucesso!");
                break;
            case 5:
                return;
            default:
                System.out.println("Opção Inválida");
        }
    }

    private static void gerenciarCategorias(ArquivoCategorias arqCategorias, ArquivoTarefas arqTarefas, Scanner scanner) throws Exception {
        int opcao;
        System.out.println("> Gerenciar Categorias");
        System.out.println("1) Criar Categoria");
        System.out.println("2) Listar Categorias");
        System.out.println("3) Excluir Categoria");
        System.out.println("4) Voltar");
        opcao = scanner.nextInt();
        scanner.nextLine();

        switch (opcao) {
            case 1: // Criar Categoria
                System.out.print("Nome da Categoria: ");
                String nomeCategoria = scanner.nextLine();
                Categoria categoria = new Categoria(-1, nomeCategoria);
                arqCategorias.create(categoria);
                System.out.println("Categoria criada com sucesso!");
                break;
            case 2: // Listar Categorias
                // arqCategorias.listarCategorias();
                break;
            case 3: // Excluir Categoria
                System.out.print("ID da Categoria para excluir: ");
                int catExcluirID = scanner.nextInt();
                boolean exclusaoCategoria = arqCategorias.deleteCategoria(catExcluirID, arqTarefas);
                if (exclusaoCategoria) {
                    System.out.println("Categoria excluída com sucesso!");
                } else {
                    System.out.println("Falha ao excluir a categoria. Verifique se não há tarefas vinculadas.");
                }
                break;
            case 4:
                return;
            default:
                System.out.println("Opção Inválida");
        }
    }
}
