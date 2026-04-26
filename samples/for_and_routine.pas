program SampleRoutine;
var
  g: Integer;

function Sum3(a, b, c: Integer): Integer;
begin
  Sum3 := a + b + c;
end;

procedure UseFor(v: Integer);
var
  i, acc: Integer;
begin
  acc := 0;
  for i := 1 to v do
    acc := acc + i;
  g := acc;
end;

begin
  g := 0;
  UseFor(5);
  WriteLn(g);
  WriteLn(Sum3(g, 2, 3));
end.
