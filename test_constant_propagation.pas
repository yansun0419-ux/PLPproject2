program TestConstantPropagation;
var
  v, x: Integer;
begin
  v := 2 * (10 + 11);
  x := 4;
  v := x + 2 * 3;
  WriteLn(v);
end.
