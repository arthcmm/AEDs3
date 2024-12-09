package aed3;

import java.io.*;
import java.nio.file.*;

public class Backup {

    public static void criarBackup(String caminhoDados, String caminhoBackup) throws IOException {
        File pastaBackup = new File(caminhoBackup);
        if (!pastaBackup.exists()) {
            pastaBackup.mkdirs();
        }

        String arquivoCompactado = caminhoBackup + "/backup.lzw";
        try (DataOutputStream saida = new DataOutputStream(new FileOutputStream(arquivoCompactado))) {
            File[] arquivos = new File(caminhoDados).listFiles();

            for (File arquivo : arquivos) {
                if (arquivo.isFile()) {
                    byte[] conteudo = Files.readAllBytes(arquivo.toPath());
                    byte[] compactado = LZW.compress(conteudo);

                    saida.writeUTF(arquivo.getName());
                    saida.writeInt(compactado.length);
                    saida.write(compactado);
                }
            }
        }
        System.out.println("Backup criado com sucesso em: " + caminhoBackup);
    }

    public static void restaurarBackup(String caminhoBackup, String caminhoRestauracao) throws IOException {
        File pastaRestauracao = new File(caminhoRestauracao);
        if (!pastaRestauracao.exists()) {
            pastaRestauracao.mkdirs();
        }

        String arquivoCompactado = caminhoBackup + "/backup.lzw";
        try (DataInputStream entrada = new DataInputStream(new FileInputStream(arquivoCompactado))) {
            while (entrada.available() > 0) {
                String nomeArquivo = entrada.readUTF();
                int tamanho = entrada.readInt();
                byte[] compactado = new byte[tamanho];
                entrada.readFully(compactado);

                byte[] descompactado = LZW.decompress(compactado);
                Files.write(Paths.get(caminhoRestauracao, nomeArquivo), descompactado);
            }
        }
        System.out.println("Arquivos restaurados com sucesso para: " + caminhoRestauracao);
    }
}
