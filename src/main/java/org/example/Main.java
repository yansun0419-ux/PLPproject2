package org.example;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            printUsage();
            return;
        }

        String command = args[0].toLowerCase();
        switch (command) {
            case "compile":
                runCompile(args);
                return;
            case "batch":
                runBatchCompile(args);
                return;
            case "interpret":
                runInterpret(args);
                return;
            default:
                printUsage();
        }
    }

    private static void runCompile(String[] args) throws Exception {
        if (args.length < 2) {
            throw new IllegalArgumentException("compile mode requires an input .pas file");
        }

        Path inputPath = Paths.get(args[1]).toAbsolutePath().normalize();
        if (!Files.exists(inputPath)) {
            throw new IllegalArgumentException("Input file not found: " + inputPath);
        }

        Path outputPath;
        if (args.length >= 3) {
            outputPath = Paths.get(args[2]).toAbsolutePath().normalize();
        } else {
            String fileName = inputPath.getFileName().toString();
            int idx = fileName.lastIndexOf('.');
            String stem = (idx > 0) ? fileName.substring(0, idx) : fileName;
            outputPath = inputPath.getParent().resolve(stem + ".ll").toAbsolutePath().normalize();
        }

        DelphiParser.ProgramContext program = parseProgram(inputPath.toFile());
        DelphiLLVMGenerator generator = new DelphiLLVMGenerator();
        String llvm = generator.generate(program);

        Files.createDirectories(outputPath.getParent());
        Files.writeString(outputPath, llvm, StandardCharsets.UTF_8);

        System.out.println("LLVM IR generated:");
        System.out.println("  input : " + inputPath);
        System.out.println("  output: " + outputPath);
    }

    private static void runBatchCompile(String[] args) throws Exception {
        if (args.length < 3) {
            throw new IllegalArgumentException("batch mode requires <inputDir> <outputDir>");
        }

        Path inputDir = Paths.get(args[1]).toAbsolutePath().normalize();
        Path outputDir = Paths.get(args[2]).toAbsolutePath().normalize();
        if (!Files.isDirectory(inputDir)) {
            throw new IllegalArgumentException("Input directory not found: " + inputDir);
        }

        Files.createDirectories(outputDir);

        List<Path> pasFiles = new ArrayList<>();
        try (var stream = Files.list(inputDir)) {
            stream.filter(Files::isRegularFile)
                    .filter(p -> p.getFileName().toString().toLowerCase().endsWith(".pas"))
                    .sorted()
                    .forEach(pasFiles::add);
        }

        if (pasFiles.isEmpty()) {
            System.out.println("No .pas files found in " + inputDir);
            return;
        }

        int ok = 0;
        int fail = 0;

        for (Path pas : pasFiles) {
            try {
                DelphiParser.ProgramContext program = parseProgram(pas.toFile());
                String llvm = new DelphiLLVMGenerator().generate(program);

                String fileName = pas.getFileName().toString();
                int idx = fileName.lastIndexOf('.');
                String stem = (idx > 0) ? fileName.substring(0, idx) : fileName;
                Path out = outputDir.resolve(stem + ".ll");

                Files.writeString(out, llvm, StandardCharsets.UTF_8);
                System.out.println("[OK ] " + pas.getFileName() + " -> " + out.getFileName());
                ok++;
            } catch (Exception ex) {
                System.out.println("[ERR] " + pas.getFileName() + " -> " + ex.getMessage());
                fail++;
            }
        }

        System.out.println("Batch compile summary: " + ok + " success, " + fail + " failed");
    }

    private static void runInterpret(String[] args) throws Exception {
        if (args.length < 2) {
            throw new IllegalArgumentException("interpret mode requires an input .pas file");
        }

        Path inputPath = Paths.get(args[1]).toAbsolutePath().normalize();
        DelphiParser.ProgramContext program = parseProgram(inputPath.toFile());
        DelphiInterpreter interpreter = new DelphiInterpreter();
        interpreter.visit(program);
    }

    private static DelphiParser.ProgramContext parseProgram(File file) throws Exception {
        try (InputStream is = new FileInputStream(file)) {
            DelphiLexer lexer = new DelphiLexer(CharStreams.fromStream(is));
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            DelphiParser parser = new DelphiParser(tokens);

            DelphiParser.ProgramContext program = parser.program();
            if (parser.getNumberOfSyntaxErrors() > 0) {
                throw new IOException("Syntax errors found while parsing: " + file.getAbsolutePath());
            }
            return program;
        }
    }

    private static void printUsage() {
        System.out.println("Usage:");
        System.out.println("  compile <input.pas> [output.ll]");
        System.out.println("  batch <inputDir> <outputDir>");
        System.out.println("  interpret <input.pas>");
    }
}