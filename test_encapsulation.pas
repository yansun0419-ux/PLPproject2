program TestEnc;

type
  TAccount = class
  private
    balance: Integer;
  public
    constructor Create();
    procedure Deposit(amt: Integer);
    function GetBalance: Integer;
  end;

var
  acc: TAccount;

constructor TAccount.Create();
begin
  balance := 0;
end;

procedure TAccount.Deposit(amt: Integer);
begin
  balance := amt;
end;

function TAccount.GetBalance: Integer;
begin
  GetBalance := balance;
end;

begin
  acc := TAccount.Create();
  acc.Deposit(500);
  WriteLn(acc.GetBalance);
end.