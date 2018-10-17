-- auto-generated definition
create sequence sequence_user
/

create or replace trigger trigger_user
  before insert
  on cse_user
  referencing new as new_user
  for each row
  begin
    if :new_user.id is null
    then
      :new_user.id := sequence_user.nextval;
    end if;
  end;