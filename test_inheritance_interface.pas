program TestBoth;

type
  IMovable = interface
    procedure Move;
  end;

  TAnimal = class
  protected
    speed: Integer;
  public
    constructor Create(s: Integer);
  end;

  TBird = class(TAnimal, IMovable)
  public
    constructor Create(s: Integer);
    procedure Move;
  end;

var
  bird: TBird;

constructor TAnimal.Create(s: Integer);
begin
  speed := s;
end;

constructor TBird.Create(s: Integer);
begin
  speed := s;
end;

procedure TBird.Move;
begin
  WriteLn(speed);
end;

begin
  bird := TBird.Create(100);
  bird.Move();
end.