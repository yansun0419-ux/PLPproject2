param(
    [Parameter(Mandatory = $true)]
    [string]$InputPas,
    [string]$OutputLl = ""
)

$ErrorActionPreference = "Stop"

mvn -q -DskipTests clean compile

if ([string]::IsNullOrWhiteSpace($OutputLl)) {
    mvn -q exec:java -Dexec.mainClass="org.example.Main" -Dexec.args="compile $InputPas"
} else {
    mvn -q exec:java -Dexec.mainClass="org.example.Main" -Dexec.args="compile $InputPas $OutputLl"
}
