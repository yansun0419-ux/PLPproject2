program TestCtor;

type
  TData = class
  private
    id: Integer;
  public
    constructor Create(newId: Integer);
    destructor Destroy;
  end;

var
  d: TData;

constructor TData.Create(newId: Integer);
begin
  id := newId;
  WriteLn(id);
end;

destructor TData.Destroy;
begin
  WriteLn(999);
end;

begin
  d := TData.Create(123);
  d.Destroy();
end.