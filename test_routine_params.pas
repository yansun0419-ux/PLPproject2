program TestRoutineParams;
var
  a, b: Integer;

function Sum3(x, y, z: Integer): Integer;
begin
  Sum3 := x + y + z;
end;

procedure SetPair(left, right: Integer);
begin
  a := left;
  b := right;
end;

begin
  a := 0;
  b := 0;

  SetPair(10, 20);
  WriteLn(a);
  WriteLn(b);
  WriteLn(Sum3(a, b, 5));
end.
