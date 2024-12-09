package aed3;

import java.io.*;
import java.util.*;

public class LZW {

    // Método para compressão
    public static byte[] compress(byte[] dados) {
        Map<String, Integer> dicionario = new HashMap<>();
        for (int i = 0; i < 256; i++) {
            dicionario.put("" + (char) i, i);
        }

        String w = "";
        List<Integer> codigos = new ArrayList<>();
        int codigoAtual = 256;

        for (byte b : dados) {
            char c = (char) (b & 0xFF); // Converte para char válido
            String wc = w + c;

            if (dicionario.containsKey(wc)) {
                w = wc;
            } else {
                if (!w.isEmpty() && dicionario.containsKey(w)) {
                    codigos.add(dicionario.get(w));
                }

                if (codigoAtual < 4096) {
                    dicionario.put(wc, codigoAtual++);
                }
                w = "" + c;
            }
        }

        // Adiciona o último código
        if (!w.isEmpty() && dicionario.containsKey(w)) {
            codigos.add(dicionario.get(w));
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (DataOutputStream dos = new DataOutputStream(baos)) {
            for (Integer codigo : codigos) {
                dos.writeShort(codigo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return baos.toByteArray();
    }



    // Método para descompressão
    public static byte[] decompress(byte[] dados) {
        List<String> dicionario = new ArrayList<>();
        for (int i = 0; i < 256; i++) {
            dicionario.add("" + (char) i);
        }

        ByteArrayInputStream bais = new ByteArrayInputStream(dados);
        List<Byte> resultado = new ArrayList<>();
        try (DataInputStream dis = new DataInputStream(bais)) {
            int codigoAntigo = dis.readShort();
            String w = dicionario.get(codigoAntigo);
            for (char c : w.toCharArray()) {
                resultado.add((byte) c);
            }

            int codigoAtual;
            while (dis.available() > 0) {
                codigoAtual = dis.readShort();

                String entrada;
                if (codigoAtual < dicionario.size()) {
                    entrada = dicionario.get(codigoAtual);
                } else if (codigoAtual == dicionario.size()) {
                    entrada = dicionario.get(codigoAntigo) + dicionario.get(codigoAntigo).charAt(0);
                } else {
                    throw new IllegalArgumentException("Código inválido durante a descompressão: " + codigoAtual);
                }

                for (char c : entrada.toCharArray()) {
                    resultado.add((byte) c);
                }

                if (dicionario.size() < 4096) {
                    dicionario.add(dicionario.get(codigoAntigo) + entrada.charAt(0));
                }

                codigoAntigo = codigoAtual;
            }
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
        }

        byte[] descompactado = new byte[resultado.size()];
        for (int i = 0; i < resultado.size(); i++) {
            descompactado[i] = resultado.get(i);
        }
        return descompactado;
    }
}
