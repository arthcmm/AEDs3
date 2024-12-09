import aed3.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Scanner;
import java.io.File;
import java.time.format.DateTimeFormatter;

public class IO {

    static ArquivoTarefas arqTarefas;
    static ArquivoCategorias arqCategorias;
    static ArquivoRotulos arqRotulos;
    public static void main(String[] args) {
        try {
            arqTarefas = new ArquivoTarefas(Tarefa.class.getConstructor(), "tarefas_.db");
            arqCategorias = new ArquivoCategorias(Categoria.class.getConstructor(), "categorias.db");
             arqRotulos = new ArquivoRotulos(Rotulo.class.getConstructor(), "rotulos.db");
            Scanner scanner = new Scanner(System.in);
            int resposta;

            while (true) {
                System.out.println("> Menu Principal");
                System.out.println("1) Gerenciar Tarefas");
                System.out.println("2) Gerenciar Categorias");
                System.out.println("3) Gerenciar Rótulos");
                System.out.println("4) Buscar Tarefas");
                System.out.println("5) Criar Backup");
                System.out.println("6) Restaurar Backup");
                System.out.println("7) Sair");
                System.out.print("Escolha uma opção: ");
                resposta = scanner.nextInt();
                scanner.nextLine();

                switch (resposta) {
                    case 1:
                        gerenciarTarefas(arqTarefas, arqCategorias, arqRotulos, scanner);
                        break;
                    case 2:
                        gerenciarCategorias(arqCategorias, arqTarefas, scanner);
                        break;
                    case 3:
                        gerenciarRotulos(arqRotulos, arqTarefas, scanner);
                        break;
                    case 4:
                        buscarTarefas(arqTarefas, scanner);
                        break;
                    case 5:
                        criarBackup(scanner);
                        break;
                    case 6:
                        restaurarBackup(scanner);
                        break;
                    case 7:
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

    // Método para gerenciar tarefas
    public static void gerenciarTarefas(ArquivoTarefas arqTarefas, ArquivoCategorias arqCategorias, ArquivoRotulos arqRotulos, Scanner scanner) throws Exception {
        int opcao;
        System.out.println("> Gerenciar Tarefas");
        System.out.println("1) Criar Tarefa");
        System.out.println("2) Listar Todas as Tarefas");
        System.out.println("3) Listar Tarefas por Categoria");
        System.out.println("4) Atualizar Tarefa");
        System.out.println("5) Excluir Tarefa");
        System.out.println("6) Vincular Rótulo");
        System.out.println("7) Listar Tarefas por Rótulo");
        System.out.println("8) Voltar");
        System.out.print("Escolha uma opção: ");
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
                int prioridade = scanner.nextInt();
                scanner.nextLine();

                System.out.println("Escolha uma categoria:");
                arqCategorias.listarCategorias();
                System.out.print("ID da Categoria: ");
                int categoriaID = scanner.nextInt();
                scanner.nextLine();

                Tarefa tarefa = new Tarefa(-1, categoriaID, nome, LocalDate.parse(data), null, status, prioridade);
                arqTarefas.create(tarefa);
                System.out.println("Tarefa criada com sucesso!");
                break;
            case 2: // Listar Todas as Tarefas
                System.out.println("Listando todas as tarefas:");
                arqTarefas.listarTodasAsTarefas();
                break;
            case 3: // Listar Tarefas por Categoria
                System.out.print("ID da Categoria para listar tarefas: ");
                int catID = scanner.nextInt();
                arqTarefas.listarTarefasPorCategoria(catID);
                break;
            case 4: // Atualizar Tarefa
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
            case 5: // Excluir Tarefa
                System.out.print("ID da Tarefa para excluir: ");
                int excluirID = scanner.nextInt();
                arqTarefas.delete(excluirID);
                System.out.println("Tarefa excluída com sucesso!");
                break;
            case 6: // Vincular Rótulo
                System.out.print("ID da Tarefa: ");
                int tarefaVincularID = scanner.nextInt();
                scanner.nextLine();
                System.out.print("ID do Rótulo: ");
                int rotuloID = scanner.nextInt();
                scanner.nextLine();
                arqTarefas.vincularRotulo(tarefaVincularID, rotuloID);
                System.out.println("Rótulo vinculado com sucesso!");
                break;
            case 7: // Listar Tarefas por Rótulo
                System.out.print("ID do Rótulo: ");
                int idRotulo = scanner.nextInt();
                for (Tarefa t : arqTarefas.listarTarefasPorRotulo(idRotulo)) {
                    System.out.println(t);
                }
                break;
            case 8:
                return;
            default:
                System.out.println("Opção Inválida");
        }
    }

    // Método para gerenciar categorias
    private static void gerenciarCategorias(ArquivoCategorias arqCategorias, ArquivoTarefas arqTarefas, Scanner scanner) throws Exception {
        int opcao;
        System.out.println("> Gerenciar Categorias");
        System.out.println("1) Criar Categoria");
        System.out.println("2) Listar Categorias");
        System.out.println("3) Excluir Categoria");
        System.out.println("4) Voltar");
        System.out.print("Escolha uma opção: ");
        opcao = scanner.nextInt();
        scanner.nextLine();

        switch (opcao) {
            case 1:
                System.out.print("Nome da Categoria: ");
                String nomeCategoria = scanner.nextLine();
                Categoria categoria = new Categoria(-1, nomeCategoria);
                arqCategorias.create(categoria);
                System.out.println("Categoria criada com sucesso!");
                break;
            case 2:
                arqCategorias.listarCategorias();
                break;
            case 3:
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

    // Método para gerenciar rótulos
    private static void gerenciarRotulos(ArquivoRotulos arqRotulos, ArquivoTarefas arqTarefas, Scanner scanner) throws Exception {
        int opcao;
        System.out.println("> Gerenciar Rótulos");
        System.out.println("1) Criar Rótulo");
        System.out.println("2) Listar Rótulos");
        System.out.println("3) Excluir Rótulo");
        System.out.println("4) Voltar");
        System.out.print("Escolha uma opção: ");
        opcao = scanner.nextInt();
        scanner.nextLine();

        switch (opcao) {
            case 1:
                System.out.print("Nome do Rótulo: ");
                String nomeRotulo = scanner.nextLine();
                Rotulo rotulo = new Rotulo(-1, nomeRotulo);
                arqRotulos.create(rotulo);
                System.out.println("Rótulo criado com sucesso!");
                break;
            case 2:
                System.out.println(arqRotulos.listarRotulos());
                break;
            case 3:
                System.out.print("ID do Rótulo para excluir: ");
                int rotuloExcluirID = scanner.nextInt();
                arqRotulos.delete(rotuloExcluirID);
                System.out.println("Rótulo excluído com sucesso!");
                break;
            case 4:
                return;
            default:
                System.out.println("Opção Inválida");
        }
    }

    // Método para buscar tarefas
    private static void buscarTarefas(ArquivoTarefas arqTarefas, Scanner scanner) throws Exception {
        System.out.println("> Buscar Tarefas");
        System.out.print("Digite o termo para buscar: ");
        String termo = scanner.nextLine();
        for (Tarefa tarefa : arqTarefas.buscarPorTermos(termo)) {
            System.out.println(tarefa);
        }
    }

    // Método para criar backup
    private static void criarBackup(Scanner scanner) {
        try {
            String caminhoDados = "src/dados";
            String dataAtual = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd--H-m-s"));
            String caminhoBackup = "src/backups/backup_" + dataAtual;

            Backup.criarBackup(caminhoDados, caminhoBackup);
            System.out.println("Backup criado com sucesso em: " + caminhoBackup);
        } catch (Exception e) {
            System.err.println("Erro ao criar backup: " + e.getMessage());
        }
    }

    // Método para restaurar backup
    private static void restaurarBackup(Scanner scanner) {
        try {
            File pastaBackup = new File("src/backups");
            File[] backups = pastaBackup.listFiles();

            if (backups == null || backups.length == 0) {
                System.out.println("Nenhum backup encontrado.");
                return;
            }

            System.out.println("> Backups Disponíveis:");
            for (int i = 0; i < backups.length; i++) {
                System.out.println((i + 1) + ") " + backups[i].getName());
            }

            System.out.print("Escolha um backup para restaurar (número): ");
            int escolha = scanner.nextInt();
            scanner.nextLine();

            if (escolha < 1 || escolha > backups.length) {
                System.out.println("Opção inválida.");
                return;
            }

            String caminhoBackupEscolhido = backups[escolha - 1].getAbsolutePath();
            String caminhoRestauracao = "src/dados";

            Backup.restaurarBackup(caminhoBackupEscolhido, caminhoRestauracao);
            System.out.println("Backup restaurado com sucesso para: " + caminhoRestauracao);
            arqTarefas = new ArquivoTarefas(Tarefa.class.getConstructor(), "tarefas_.db");
            arqCategorias = new ArquivoCategorias(Categoria.class.getConstructor(), "categorias.db");
            arqRotulos = new ArquivoRotulos(Rotulo.class.getConstructor(), "rotulos.db");
        } catch (Exception e) {
            System.err.println("Erro ao restaurar backup: " + e.getMessage());
        }
    }
}
