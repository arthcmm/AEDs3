import aed3.ArquivoCategorias;
import aed3.ArquivoTarefas;
import aed3.Categoria;
import aed3.Tarefa;

import java.util.Scanner;

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

                switch (resposta) {
                    case 1:
                        gerenciarTarefas(arqTarefas, arqCategorias, scanner);
                        break;
                    case 2:
                        gerenciarCategorias(arqCategorias, arqTarefas, scanner);
                        break;
                    case 3:
                        System.out.println("Saindo...");
                        scanner.close();
                        System.exit(0);
                    default:
                        System.out.println("Opção Inválida");
                }
            }

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
