package org.example.capstone_project.domain.food;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class PythonRun {
    public static String runPythonScript(String targetFood) {
        try {
            String scriptPath = System.getenv("SIMILARITY_PY_PATH");
            if (scriptPath == null || scriptPath.isEmpty()) {
                throw new IllegalStateException("SIMILARITY_PY_PATH 환경변수가 설정되지 않았습니다.");
            }

            ProcessBuilder pb = new ProcessBuilder("/usr/bin/python3", scriptPath, targetFood);
            pb.redirectErrorStream(true);
            Process process = pb.start();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8)
            );

            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line);
            }

            int exitCode = process.waitFor();
            if (exitCode != 0 || output.toString().contains("Traceback")) {
                System.err.println("⚠️ Python 실행 중 오류 발생:\n" + output);
                return null;
            }

            return output.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}