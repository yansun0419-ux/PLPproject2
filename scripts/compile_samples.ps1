param(
    [string]$InputDir = "samples",
    [string]$OutputDir = "llvm_out"
)

$ErrorActionPreference = "Stop"

Write-Host "Compiling Java project..."
mvn -q -DskipTests clean compile

Write-Host "Generating LLVM IR files..."
mvn -q exec:java -Dexec.mainClass="org.example.Main" -Dexec.args="batch $InputDir $OutputDir"

Write-Host "Done. LLVM output folder: $OutputDir"
