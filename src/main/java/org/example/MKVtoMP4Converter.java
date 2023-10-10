package org.example;

import java.io.IOException;

public class MKVtoMP4Converter {
    public static void main(String[] args) {
        String ffmpegPath = "C:/ffmpeg/bin/ffmpeg.exe"; // Substitua pel
        String inputFilePath = "C:/Videos/teste.mkv";
        String outputFilePath = "C:/Videos/teste2.mp4";

        try {
            String ffmpegCommand =  ffmpegPath +" -i " + inputFilePath + " -c:v copy -c:a copy " + outputFilePath;
            Process process = new ProcessBuilder("cmd.exe", "/c", ffmpegCommand)
               .redirectErrorStream(true)
               .start();

            // Lê a saída do processo para acompanhar o progresso
            StreamGobbler streamGobbler = new StreamGobbler(process.getInputStream());
            Thread thread = new Thread(streamGobbler);
            thread.start();

            int exitCode = process.waitFor();
            thread.join(); // Aguarda a leitura da saída ser concluída

            if (exitCode == 0) {
                System.out.println("Conversão concluída com sucesso.");
            } else {
                System.err.println("A conversão falhou com código de saída: " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
class StreamGobbler implements Runnable {
    private final java.io.InputStream inputStream;

    public StreamGobbler(java.io.InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public void run() {
        try {
            java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                // Exibe a saída do processo no console
                System.out.println(line);
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
}
