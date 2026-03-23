program TestObject;
type
  TPoint = class
  public
    x: Integer;
    constructor Create();
    procedure SetX(val: Integer);
  end;
var
  pt: TPoint;

constructor TPoint.Create();
begin
  x := 0;
end;

procedure TPoint.SetX(val: Integer);
begin
  x := val;
end;

begin
  pt := TPoint.Create();
  pt.SetX(150);
  WriteLn(pt.x);
end.