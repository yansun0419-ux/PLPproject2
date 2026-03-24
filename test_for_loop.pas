program TestForLoop;
var
  i, up, down: Integer;
begin
  up := 0;
  for i := 1 to 4 do
    up := up + i;

  down := 0;
  for i := 4 downto 1 do
    down := down + i;

  WriteLn(up);
  WriteLn(down);
end.
