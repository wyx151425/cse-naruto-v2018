create sequence sequence_user
  increment by 1
  start with 1
  maxvalue 999999999;

create sequence sequence_statistic
  increment by 1
  start with 1
  maxvalue 999999999;

create sequence sequence_permission
  increment by 1
  start with 1
  maxvalue 999999999;

create sequence sequence_material
  increment by 1
  start with 1
  maxvalue 999999999;

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

create or replace trigger trigger_permission
  before insert
  on cse_permission
  referencing new as new_permission
  for each row
  begin
    if :new_permission.id is null
    then
      :new_permission.id := sequence_permission.nextval;
    end if;
  end;

create or replace trigger trigger_material
  before insert
  on cse_material
  referencing new as new_material
  for each row
  begin
    if :new_material.id is null
    then
      :new_material.id := sequence_material.nextval;
    end if;
  end;

CREATE TABLE cse_material (
  id CHAR(32) PRIMARY KEY,
  status NUMBER(1),
  create_at DATETIME_INTERVAL_CODE,
  update_at DATETIME_INTERVAL_CODE,
  number VARCHAR2(255),
  rank NUMBER(2),
  material VARCHAR2(255),
  paint VARCHAR2(255),
  parent_id CHAR(32));
