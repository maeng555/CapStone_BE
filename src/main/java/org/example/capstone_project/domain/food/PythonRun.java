package org.example.capstone_project.domain.food;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class PythonRun {
    public static String runPythonScript(String targetFood) throws IOException {
        String path = "/Users/maengjin-yeong/Documents/capstone_project/capstone_project/src/main/java/org/example/capstone_project/python/embedding/similarity.py";
        ProcessBuilder pb = new ProcessBuilder("/opt/homebrew/bin/python3", path, targetFood);  // python3
        pb.redirectErrorStream(true);
        Process process = pb.start();

        // BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8)
        );

        StringBuilder output = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            output.append(line);
        }

        return output.toString();  // JSON string

    }
}


// 아래는 gpt가 해서 어려움 근데 더 안전함
//package com.example.capstone.util;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.nio.charset.StandardCharsets;
//
//public class PythonRun {
//
//    public static String runPythonScript(String targetFood) throws Exception {
//        String path = "C:\\capstone\\capstone\\similarity.py";     // 파이썬파일 있는 절대경로
//        ProcessBuilder pb = new ProcessBuilder("python", path, targetFood);  // python3
//        Process process = pb.start();
//
//        // 표준 출력만 읽기 (에러는 무시하거나 따로 처리 가능)
//        BufferedReader reader = new BufferedReader(
//                new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
//
//        StringBuilder output = new StringBuilder();
//        String line;
//        while ((line = reader.readLine()) != null) {
//            output.append(line);
//        }
//
//        int exitCode = process.waitFor();
//        if (exitCode != 0) {
//            // 필요하면 에러 처리: 에러 스트림 읽기
//            BufferedReader errorReader = new BufferedReader(
//                    new InputStreamReader(process.getErrorStream(), StandardCharsets.UTF_8));
//            StringBuilder errorOutput = new StringBuilder();
//            while ((line = errorReader.readLine()) != null) {
//                errorOutput.append(line).append("\n");
//            }
//            throw new RuntimeException("Python script error: " + errorOutput.toString());
//        }
//
//        return output.toString();
//    }
//}

