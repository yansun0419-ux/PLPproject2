program FinalProject;

type
  TCalculator = class
  private
    total: Integer;
  public
    constructor Create(v: Integer);
    procedure Add(v: Integer);
    function GetRes: Integer;
  end;

var
  calc: TCalculator;
  val: Integer;

constructor TCalculator.Create(v: Integer);
begin
  total := v;
end;

procedure TCalculator.Add(v: Integer);
begin
  total := total + v;
end;

function TCalculator.GetRes: Integer;
begin
  GetRes := total;
end;

begin
  val := 10; { Assume ReadInt() returns 10 }
  calc := TCalculator.Create(val);
  calc.Add(10);
  WriteLn(100);
end.