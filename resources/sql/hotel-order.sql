drop table if exists cho_order;
CREATE TABLE cho_order
(
    order_id       varchar(64) primary key,
    user_id        bigint(20)                                                   not null,
    type_id        bigint(20)                                                   not null,
    room_id        bigint(20)                                                   not null, -- room
    order_status   integer                                                      not null, -- 0 booking, 1 isPayed, 2 isCheckIn, 3 isLeaved, 4 isCancelled
    start_date     date                                                         not null,
    end_date       date                                                         not null,
    origin_money   decimal(18, 2)                                               not null,
    after_discount decimal(18, 2)                                               not null,
    additional     varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    score          integer,
    hotel_id       bigint(20)                                                   not null,
    contact_name   varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    contact_phone  varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
    contact_email  varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

drop table if exists cho_order_info;
CREATE TABLE cho_order_info
(
    order_id      varchar(64)                                                  not null,
    tenant_name   varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    identity_card char(18)                                                     not null,
    telephone     varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

drop table if exists cho_order_operation;
CREATE TABLE cho_order_operation
(
    order_id       varchar(64) not null,
    operation      int         not null, -- 0 booking, 1 payed, 2 checkIn, 3 leave, 4 cancel
    operation_time datetime    not null,
    primary key (order_id, operation)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

drop table if exists cho_order_comments;
CREATE TABLE cho_order_comments
(
    order_id     varchar(64) not null,
    comments     text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    comment_time datetime    not null,
    picture      varchar(1000),
    video        varchar(1000)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

drop table if exists cho_income_refund;
CREATE TABLE cho_income_refund
(
    order_id varchar(64)    not null,
    income   decimal(18, 2) not null, -- positive means income, negative means outcome
    time     datetime       not null
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

drop table if exists cho_booking;
CREATE TABLE cho_booking
(                                     -- this table will be updated every day, if end_time passed, the item will be deleted
    user_id     bigint(20)  not null,
    order_id    varchar(64) not null,
    hotel_id    bigint(20)  not null,
    type_id     bigint(20)  not null,
    room_id     bigint(20)  not null, -- room_id
    start_date  date        not null,
    end_date    date        not null,
    order_state boolean     not null
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

