program TestAccessControl;

type
  TBox = class
  private
    secret: Integer;
  protected
    protectedValue: Integer;
  public
    publicValue: Integer;
    constructor Init(v: Integer);
    procedure TestAccess;
    destructor Destroy;
  end;

var
  b: TBox;

constructor TBox.Init(v: Integer);
begin
  secret := v;
  protectedValue := v * 2;
  publicValue := v * 3;
end;

procedure TBox.TestAccess;
begin
  WriteLn(secret);
  WriteLn(protectedValue);
  WriteLn(publicValue);
end;

destructor TBox.Destroy;
begin
  WriteLn(0);
end;

begin
  b := TBox.Init(10);
  b.TestAccess;
  WriteLn(b.publicValue);
  b.Destroy();
end.