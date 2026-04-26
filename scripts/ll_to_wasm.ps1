param(
    [Parameter(Mandatory = $true)]
    [string]$InputLl,
    [string]$OutputWasm = ""
)

$ErrorActionPreference = "Stop"

$clang = "C:\Program Files\LLVM\bin\clang.exe"
if (-not (Test-Path $clang)) {
    throw "clang not found at $clang"
}

if ([string]::IsNullOrWhiteSpace($OutputWasm)) {
    $OutputWasm = [System.IO.Path]::ChangeExtension($InputLl, ".wasm")
}

$args = @(
    "--target=wasm32",
    "-nostdlib",
    "-Wl,--no-entry",
    "-Wl,--allow-undefined",
    "-Wl,--export=main",
    "-o", $OutputWasm,
    $InputLl
)

& $clang @args