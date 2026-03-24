program TestRoutinesScope;
var
  x, y: Integer;

procedure SetGlobalTo(value: Integer);
begin
  x := value;
end;

function AddToGlobal(delta: Integer): Integer;
var
  x: Integer;
begin
  x := 100;
  AddToGlobal := x + delta;
end;

procedure Caller();
var
  x: Integer;
begin
  x := 7;
  SetGlobalTo(42);
  y := AddToGlobal(1);
  WriteLn(x);
end;

begin
  x := 1;
  y := 0;
  Caller();
  WriteLn(x);
  WriteLn(y);
end.
