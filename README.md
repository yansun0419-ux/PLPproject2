# PLP Project 3: Delphi/Turbo Pascal to LLVM IR

## Goal
This branch is prepared for a high-score Project 3 path:
- Parse Delphi/Turbo Pascal with ANTLR4
- Generate LLVM IR (`.ll`) from supported language constructs
- Keep interpreter mode available for semantic cross-checking

## Current Compiler Coverage
The LLVM generator currently supports:
- Global integer variables
- Local integer variables
- Assignments and arithmetic (`+`, `-`, `*`, `/`)
- Relational operators (`=`, `<>`, `<`, `>`, `<=`, `>=`)
- Boolean connectives (`and`, `or`)
- `if/then/else`
- `while`, `for ... to`, `for ... downto`
- `break`, `continue`
- Global procedures/functions with formal parameters
- Built-ins: `ReadInt`, `WriteLn`

Not yet in LLVM mode:
- Class/object member access and method dispatch
- Interface/inheritance lowering

Those object-oriented features still exist in the interpreter path (`interpret` mode), but are not yet lowered to LLVM IR.

## CLI Modes
Entry point: `src/main/java/org/example/Main.java`

Supported commands:
```bash
compile <input.pas> [output.ll]
batch <inputDir> <outputDir>
interpret <input.pas>
```

Examples:
```bash
# compile one file
mvn exec:java -Dexec.mainClass="org.example.Main" -Dexec.args="compile samples/loop_control.pas"

# compile all .pas in a folder
mvn exec:java -Dexec.mainClass="org.example.Main" -Dexec.args="batch samples llvm_out"

# run interpreter mode
mvn exec:java -Dexec.mainClass="org.example.Main" -Dexec.args="interpret samples/for_and_routine.pas"
```

## Quick Build Scripts
- `scripts/compile_samples.ps1`
- `scripts/build_and_compile_one.ps1`

PowerShell usage:
```powershell
./scripts/compile_samples.ps1
./scripts/build_and_compile_one.ps1 -InputPas samples/loop_control.pas
```

## Workspace Layout
- Grammar: `src/main/antlr4/org/example/Delphi.g4`
- Interpreter: `src/main/java/org/example/DelphiInterpreter.java`
- LLVM generator: `src/main/java/org/example/DelphiLLVMGenerator.java`
- CLI entry: `src/main/java/org/example/Main.java`
- Sample Pascal programs: `samples/*.pas`

## LLVM to WASM (EC Path)
This part now has a practical local flow.

Recommended command flow:
```bash
# 1) generate .ll
mvn exec:java -Dexec.mainClass="org.example.Main" -Dexec.args="compile samples/loop_control.pas llvm_out/loop_control.ll"

# 2) ll -> wasm using LLVM
PowerShell: .\scripts\ll_to_wasm.ps1 -InputLl llvm_out/loop_control.ll -OutputWasm web/loop_control.wasm
```

The browser loader in `web/run.js` provides `printf` and `scanf` imports, and uses the module's exported memory to display output.

To demo:
1. Copy or generate `web/loop_control.wasm`
2. Serve the repo as a static site
3. Open `web/index.html`
4. Run the wasm file from the page

## Submission Evidence Checklist
For top-score submission quality, include at least:
- Source code of parser + LLVM generator
- A reproducible command list (already in this README)
- Multiple input `.pas` files
- Generated `.ll` artifacts for each sample you claim
- If doing EC: generated `.wasm` and browser loader files
- Demo video(s):
  - base flow: `.pas -> .ll -> run`
  - EC flow: `.ll -> .wasm -> browser`

## Build Requirements
- Java 22 (matching `pom.xml`)
- Maven 3.6+

Compile command:
```bash
mvn clean compile
```
