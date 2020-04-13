create table if not exists flight (
    id                  integer         not null identity,
    origin              varchar(200)    not null,
    destination         varchar(200)    not null,
    scheduled_at        timestamp       not null,
    created_by          varchar(200)    not null,
    created_date        timestamp       not null,
    last_modified_by     varchar(200)    not null,
    last_modified_date   timestamp       not null
);

create table if not exists purchase_order (
  id                    integer         not null identity,
  shipping_address      varchar(200)    not null
);

create table if not exists order_item (
  purchase_order        integer         not null,
  quantity              integer         not null,
  product               varchar(200)    not null
);

create table if not exists book (
  id                    integer         not null identity,
  title                 varchar(200)    not null
);

create table if not exists author (
  id                    integer         not null identity,
  name                  varchar(200)    not null
);

create table if not exists book_author (
  book                  integer         not null,
  author                integer         not null
);
