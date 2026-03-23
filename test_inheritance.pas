program TestInheritance;

type
  TAnimal = class
  protected
    name: Integer;
  public
    constructor Create(n: Integer);
    procedure Speak;
  end;

  TDog = class(TAnimal)
  public
    constructor Create(n: Integer);
    procedure Speak;
  end;

var
  dog: TDog;

constructor TAnimal.Create(n: Integer);
begin
  name := n;
  WriteLn(100);
end;

procedure TAnimal.Speak;
begin
  WriteLn(200);
end;

constructor TDog.Create(n: Integer);
begin
  name := n;
  WriteLn(300);
end;

procedure TDog.Speak;
begin
  WriteLn(400);
end;

begin
  dog := TDog.Create(1);
  dog.Speak;
  WriteLn(dog.name);
end.