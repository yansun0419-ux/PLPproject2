# Project 2 Requirement Mapping

This file maps assignment requirements to implementation evidence in this repository.

## Core Requirement: while-do and for-do loops

- Grammar support:
  - src/main/antlr4/org/example/Delphi.g4
    - whileStatement
    - forStatement
- Runtime support:
  - src/main/java/org/example/DelphiInterpreter.java
    - visitWhileStatement
    - visitForStatement
- Tests:
  - test_loop_control.pas
  - test_for_loop.pas

## Core Requirement: break and continue

- Grammar support:
  - src/main/antlr4/org/example/Delphi.g4
    - breakStatement
    - continueStatement
- Runtime support:
  - src/main/java/org/example/DelphiInterpreter.java
    - BreakSignal / ContinueSignal
    - visitBreakStatement
    - visitContinueStatement
- Tests:
  - test_loop_control.pas

## Core Requirement: user-defined procedures and functions

- Grammar support:
  - src/main/antlr4/org/example/Delphi.g4
    - routineImplementation
- Runtime support:
  - src/main/java/org/example/DelphiInterpreter.java
    - routineBodies registry
    - visitRoutineImplementation
    - executeRoutine
- Tests:
  - test_routines_scope.pas
  - test_routine_params.pas

## Core Requirement: static scoping

- Runtime support:
  - src/main/java/org/example/DelphiInterpreter.java
    - Scope chain structure
    - currentScope tracking
    - function/procedure execution rooted at global scope
- Tests:
  - test_routines_scope.pas

## Requirement: scope creation in loops and blocks

- Runtime support:
  - src/main/java/org/example/DelphiInterpreter.java
    - visitCompoundStatement
    - visitWhileStatement
    - visitForStatement

## Requirement: reuse Project 1 functionality

- Existing tests retained:
  - test_1.pas
  - test_class_object.pas
  - test_constructor_destructor.pas
  - test_encapsulation.pas
  - test_access_control.pas
  - test_io.pas
  - test_inheritance.pas
  - test_interface.pas
  - test_inheritance_interface.pas
  - test_full_program.pas

## Bonus: formal parameter passing

- Runtime support:
  - src/main/java/org/example/DelphiInterpreter.java
    - bindFormalParameters
- Tests:
  - test_routine_params.pas

## Bonus: simple constant propagation

- Runtime support:
  - src/main/java/org/example/DelphiInterpreter.java
    - foldExpression / foldRelExpression / foldAdditiveExpression / foldTerm / foldFactor
    - assignment optimization output with [AST-OPT]
- Tests:
  - test_constant_propagation.pas

## Batch test entry point

- src/main/java/org/example/Main.java
  - includes both Project 1 and Project 2 test files.

## Build and run

- mvn clean compile
- mvn org.codehaus.mojo:exec-maven-plugin:3.6.1:java -Dexec.mainClass=org.example.Main
