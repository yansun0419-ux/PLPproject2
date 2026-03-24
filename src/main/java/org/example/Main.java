package org.example;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        String[] testFiles = {
                "test_full_program.pas",
                "test_1.pas",
                "test_class_object.pas",
                "test_constructor_destructor.pas",
                "test_encapsulation.pas",
                "test_access_control.pas",
                "test_io.pas",
                "test_inheritance.pas",           // Inheritance test
                "test_interface.pas",             // Interface test
            "test_inheritance_interface.pas", // Inheritance + interface test
            "test_loop_control.pas",          // Project 2: while + break + continue
            "test_for_loop.pas",              // Project 2: for-to and for-downto
            "test_routines_scope.pas",        // Project 2: procedures/functions + static scoping
            "test_routine_params.pas"         // Project 2 bonus: formal parameter passing
        };

        int passedCount = 0;
        int failedCount = 0;

        System.out.println("==============================================");
        System.out.println("Starting batch tests for the Delphi interpreter (Project 1 + Project 2)");
        System.out.println("==============================================\n");

        for (String testFile : testFiles) {
            System.out.println("\n----------------------------------------------");
            System.out.println("Test file: " + testFile);
            System.out.println("----------------------------------------------");

            File file = new File(testFile);
            if (!file.exists()) {
                System.err.println("Warning: test file not found " + file.getAbsolutePath());
                System.out.println("Status: SKIPPED\n");
                failedCount++;
                continue;
            }

            try (InputStream is = new FileInputStream(testFile)) {
                DelphiLexer lexer = new DelphiLexer(CharStreams.fromStream(is));
                CommonTokenStream tokens = new CommonTokenStream(lexer);
                DelphiParser parser = new DelphiParser(tokens);

                ParseTree tree = parser.program();

                if (parser.getNumberOfSyntaxErrors() > 0) {
                    System.err.println("Parse failed: syntax errors found");
                    System.out.println("Status: FAILED\n");
                    failedCount++;
                } else {
                    System.out.println("Parse succeeded");
                    System.out.println(">>> Program output:");
                    System.out.println("-----------------------");

                    DelphiInterpreter interpreter = new DelphiInterpreter();

                    try {
                        interpreter.visit(tree);
                        System.out.println("-----------------------");
                        System.out.println("Status: PASSED\n");
                        passedCount++;
                    } catch (Exception e) {
                        System.out.println("-----------------------");
                        System.err.println("Runtime error: " + e.getMessage());
                        e.printStackTrace();
                        System.out.println("Status: FAILED\n");
                        failedCount++;
                    }
                }
            } catch (Exception e) {
                System.err.println("File read error:");
                e.printStackTrace();
                System.out.println("Status: FAILED\n");
                failedCount++;
            }
        }

        System.out.println("\n==============================================");
        System.out.println("Test summary");
        System.out.println("==============================================");
        System.out.println("Total tests: " + testFiles.length + " files");
        System.out.println("Passed: " + passedCount);
        System.out.println("Failed: " + failedCount);
        System.out.println("Pass rate: " + String.format("%.1f%%", (passedCount * 100.0 / testFiles.length)));
        System.out.println("==============================================");
    }
}