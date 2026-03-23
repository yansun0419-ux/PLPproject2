program FullProgram;
type
  TBox = class
    val: Integer;
    constructor Init(v: Integer);
    procedure DoubleVal();
  end;

var
  myBox: TBox;
  inputVal: Integer;

constructor TBox.Init(v: Integer);
begin
  val := v;
end;

procedure TBox.DoubleVal();
begin
  val := val * 2;
end;

begin
  inputVal := 100;

  myBox := TBox.Init(inputVal);

  myBox.DoubleVal();

  WriteLn(myBox.val);
end.