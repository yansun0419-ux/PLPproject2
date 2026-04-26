program SampleLoop;
var
  i, sum: Integer;
begin
  i := 0;
  sum := 0;

  while i < 10 do
  begin
    i := i + 1;

    if i = 3 then
      continue;

    if i = 8 then
      break;

    sum := sum + i;
  end;

  WriteLn(sum);
end.
