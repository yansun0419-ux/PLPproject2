program TestInterface;

type
  ISpeakable = interface
    procedure Speak;
  end;

  TCat = class(ISpeakable)
  public
    constructor Create;
    procedure Speak;
  end;

var
  cat: TCat;

constructor TCat.Create;
begin
  WriteLn(100);
end;

procedure TCat.Speak;
begin
  WriteLn(200);
end;

begin
  cat := TCat.Create();
  cat.Speak();
end.