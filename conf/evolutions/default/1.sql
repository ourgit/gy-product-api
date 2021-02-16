# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

-- init script create procs
-- Inital script to create stored procedures etc for mysql platform
DROP PROCEDURE IF EXISTS usp_ebean_drop_foreign_keys;

delimiter $$
--
-- PROCEDURE: usp_ebean_drop_foreign_keys TABLE, COLUMN
-- deletes all constraints and foreign keys referring to TABLE.COLUMN
--
CREATE PROCEDURE usp_ebean_drop_foreign_keys(IN p_table_name VARCHAR(255), IN p_column_name VARCHAR(255))
BEGIN
  DECLARE done INT DEFAULT FALSE;
  DECLARE c_fk_name CHAR(255);
  DECLARE curs CURSOR FOR SELECT CONSTRAINT_NAME from information_schema.KEY_COLUMN_USAGE
    WHERE TABLE_SCHEMA = DATABASE() and TABLE_NAME = p_table_name and COLUMN_NAME = p_column_name
      AND REFERENCED_TABLE_NAME IS NOT NULL;
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

  OPEN curs;

  read_loop: LOOP
    FETCH curs INTO c_fk_name;
    IF done THEN
      LEAVE read_loop;
    END IF;
    SET @sql = CONCAT('ALTER TABLE ', p_table_name, ' DROP FOREIGN KEY ', c_fk_name);
    PREPARE stmt FROM @sql;
    EXECUTE stmt;
  END LOOP;

  CLOSE curs;
END
$$

DROP PROCEDURE IF EXISTS usp_ebean_drop_column;

delimiter $$
--
-- PROCEDURE: usp_ebean_drop_column TABLE, COLUMN
-- deletes the column and ensures that all indices and constraints are dropped first
--
CREATE PROCEDURE usp_ebean_drop_column(IN p_table_name VARCHAR(255), IN p_column_name VARCHAR(255))
BEGIN
  CALL usp_ebean_drop_foreign_keys(p_table_name, p_column_name);
  SET @sql = CONCAT('ALTER TABLE ', p_table_name, ' DROP COLUMN ', p_column_name);
  PREPARE stmt FROM @sql;
  EXECUTE stmt;
END
$$
create table v1_balance_log (
  id                            bigint auto_increment not null,
  uid                           bigint not null,
  item_id                       integer not null,
  left_balance                  bigint not null,
  freeze_balance                bigint not null,
  total_balance                 bigint not null,
  change_amount                 bigint not null,
  biz_type                      integer not null,
  note                          varchar(255),
  create_time                   bigint not null,
  constraint pk_v1_balance_log primary key (id)
);

create table v1_brand (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  url                           varchar(255),
  logo                          varchar(255),
  poster                        varchar(255),
  content                       varchar(255),
  sort                          integer not null,
  status                        integer not null,
  seo_title                     varchar(255),
  seo_keywords                  varchar(255),
  seo_description               varchar(255),
  show_at_home                  tinyint(1) default 0 not null,
  update_time                   bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_brand primary key (id)
);

create table v1_category (
  id                            bigint auto_increment not null,
  parent_id                     bigint not null,
  name                          varchar(255),
  img_url                       varchar(255),
  poster                        varchar(255),
  path                          varchar(255),
  is_shown                      integer not null,
  sort                          integer not null,
  sold_amount                   bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_category primary key (id)
);

create table v1_category_attr (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  sort                          integer not null,
  constraint pk_v1_category_attr primary key (id)
);

create table v1_charge (
  id                            bigint auto_increment not null,
  transaction_id                varchar(255),
  sub_id                        varchar(255),
  uid                           bigint not null,
  amount                        integer not null,
  status                        integer not null,
  pay_type                      integer not null,
  update_time                   bigint not null,
  constraint pk_v1_charge primary key (id)
);

create table v1_comment (
  id                            bigint auto_increment not null,
  product_id                    bigint not null,
  order_id                      bigint not null,
  content                       varchar(255),
  level                         integer not null,
  desc_star                     integer not null,
  logistics_star                integer not null,
  service_star                  integer not null,
  uid                           bigint not null,
  name                          varchar(255),
  type                          integer not null,
  reply_id                      bigint not null,
  has_append                    tinyint(1) default 0 not null,
  update_time                   bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_comment primary key (id)
);

create table v1_contact_detail (
  id                            bigint auto_increment not null,
  uid                           bigint not null,
  name                          varchar(255),
  province                      varchar(255),
  province_code                 varchar(255),
  city                          varchar(255),
  city_code                     varchar(255),
  area                          varchar(255),
  area_code                     varchar(255),
  details                       varchar(255),
  postcode                      varchar(255),
  telephone                     varchar(255),
  is_default                    integer not null,
  update_time                   bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_contact_detail primary key (id)
);

create table v1_coupon_config (
  id                            bigint auto_increment not null,
  coupon_title                  varchar(255),
  coupon_content                varchar(255),
  amount                        integer not null,
  type                          integer not null,
  status                        integer not null,
  claim_per_member              integer not null,
  total_amount                  integer not null,
  claim_amount                  integer not null,
  id_type                       integer not null,
  rule_content                  varchar(255),
  merchant_ids                  varchar(255),
  brand_ids                     varchar(255),
  img_url                       varchar(255),
  begin_time                    bigint not null,
  end_time                      bigint not null,
  expire_days                   bigint not null,
  old_price                     integer not null,
  current_price                 integer not null,
  update_time                   bigint not null,
  constraint pk_v1_coupon_config primary key (id)
);

create table v1_dealer (
  id                            bigint auto_increment not null,
  type                          integer not null,
  dealer_name                   varchar(255),
  dealer_contact_detail         varchar(255),
  join_time                     bigint not null,
  description                   varchar(255),
  likes                         bigint not null,
  update_time                   bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_dealer primary key (id)
);

create table v1_dict (
  id                            bigint auto_increment not null,
  type                          integer not null,
  value                         varchar(255),
  update_time                   bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_dict primary key (id)
);

create table v1_flash_sale (
  id                            bigint auto_increment not null,
  display_time                  varchar(255),
  begintime                     bigint not null,
  endtime                       bigint not null,
  status                        integer not null,
  constraint pk_v1_flash_sale primary key (id)
);

create table v1_flash_sale_product (
  id                            bigint auto_increment not null,
  product_id                    bigint not null,
  flash_sale_id                 bigint not null,
  product_head_pic              varchar(255),
  title                         varchar(255),
  price                         bigint not null,
  total_count                   bigint not null,
  sold_count                    bigint not null,
  begin_time                    bigint not null,
  end_time                      bigint not null,
  duration                      bigint not null,
  status                        integer not null,
  sort                          bigint not null,
  constraint pk_v1_flash_sale_product primary key (id)
);

create table v1_mail_fee_config (
  id                            integer auto_increment not null,
  region_code                   varchar(255),
  region_name                   varchar(255),
  fee                           integer not null,
  up_to                         integer not null,
  update_time                   bigint not null,
  constraint pk_v1_mail_fee_config primary key (id)
);

create table v1_member (
  id                            bigint auto_increment not null,
  login_password                varchar(255),
  pay_password                  varchar(255),
  status                        integer not null,
  real_name                     varchar(255),
  nick_name                     varchar(255),
  phone_number                  varchar(255),
  description                   varchar(255),
  birthday                      bigint not null,
  update_time                   bigint not null,
  create_time                   bigint not null,
  allow_publish                 tinyint(1) default 0 not null,
  level                         integer not null,
  agent_code                    varchar(255),
  id_card_no                    varchar(255),
  open_id                       varchar(255),
  union_id                      varchar(255),
  session_key                   varchar(255),
  gender                        integer not null,
  city                          varchar(255),
  province                      varchar(255),
  country                       varchar(255),
  country_code                  varchar(255),
  agent_id                      bigint not null,
  avatar                        varchar(255),
  sign_phase                    varchar(255),
  follow_count                  bigint not null,
  fans_count                    bigint not null,
  favs_count                    bigint not null,
  bg_img_url                    varchar(255),
  continuation_sign_days        bigint not null,
  constraint pk_v1_member primary key (id)
);

create table v1_member_balance (
  id                            bigint auto_increment not null,
  uid                           bigint not null,
  item_id                       integer not null,
  left_balance                  double not null,
  freeze_balance                double not null,
  total_balance                 double not null,
  update_time                   bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_member_balance primary key (id)
);

create table v1_member_coupon (
  id                            bigint auto_increment not null,
  uid                           bigint not null,
  coupon_id                     bigint not null,
  begin_time                    bigint not null,
  end_time                      bigint not null,
  status                        bigint not null,
  code                          varchar(255),
  tx_id                         varchar(255),
  sub_id                        varchar(255),
  pay_type                      integer not null,
  real_pay                      integer not null,
  update_time                   bigint not null,
  constraint pk_v1_member_coupon primary key (id)
);

create table v1_member_level (
  id                            integer auto_increment not null,
  need_score                    bigint not null,
  level                         integer not null,
  level_name                    varchar(255),
  order_discount                integer not null,
  update_time                   bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_member_level primary key (id)
);

create table v1_member_like (
  id                            bigint auto_increment not null,
  member_id                     bigint not null,
  type                          integer not null,
  target_id                     bigint not null,
  update_time                   bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_member_like primary key (id)
);

create table v1_member_score_config (
  id                            bigint auto_increment not null,
  type                          integer not null,
  description                   varchar(255),
  score                         bigint not null,
  update_time                   bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_member_score_config primary key (id)
);

create table v1_member_score_log (
  id                            bigint auto_increment not null,
  member_id                     bigint not null,
  score                         bigint not null,
  type                          integer not null,
  reason_type                   integer not null,
  description                   varchar(255),
  create_time                   bigint not null,
  constraint pk_v1_member_score_log primary key (id)
);

create table v1_mix_option (
  id                            integer auto_increment not null,
  amount                        integer not null,
  mix_code                      varchar(255),
  constraint pk_v1_mix_option primary key (id)
);

create table v1_operation_log (
  id                            bigint auto_increment not null,
  admin_id                      bigint not null,
  admin_name                    varchar(255),
  ip                            varchar(255),
  place                         varchar(255),
  note                          varchar(255),
  create_time                   bigint not null,
  constraint pk_v1_operation_log primary key (id)
);

create table v1_order (
  id                            bigint auto_increment not null,
  uid                           bigint not null,
  user_name                     varchar(255),
  order_no                      varchar(255),
  tx_id                         varchar(255),
  org_id                        bigint not null,
  org_name                      varchar(255),
  score_gave                    bigint not null,
  status                        integer not null,
  post_service_status           integer not null,
  product_count                 integer not null,
  total_money                   double not null,
  real_pay                      double not null,
  logistics_fee                 double not null,
  address                       varchar(255),
  pay_method                    integer not null,
  out_trade_no                  varchar(255),
  pay_tx_no                     varchar(255),
  pay_time                      bigint not null,
  delivery_time                 bigint not null,
  score_use                     integer not null,
  score_to_money                double not null,
  order_settlement_time         bigint not null,
  is_mix                        tinyint(1) default 0 not null,
  coupon_id                     bigint not null,
  coupon_free                   double not null,
  commission_handled            tinyint(1) default 0 not null,
  region_path                   varchar(255),
  refund_tx_id                  varchar(255),
  update_time                   bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_order primary key (id)
);

create table v1_order_detail (
  id                            bigint auto_increment not null,
  order_id                      bigint not null,
  product_id                    bigint not null,
  product_name                  varchar(255),
  product_price                 double not null,
  sku_id                        bigint not null,
  sku_name                      varchar(255),
  unit                          varchar(255),
  product_img_url               varchar(255),
  product_mode_desc             varchar(255),
  product_mode_params           varchar(255),
  discount_rate                 integer not null,
  discount_amount               double not null,
  number                        bigint not null,
  sub_total                     double not null,
  is_product_available          tinyint(1) default 0 not null,
  remark                        varchar(255),
  update_time                   bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_order_detail primary key (id)
);

create table v1_org (
  id                            integer auto_increment not null,
  status                        integer not null,
  name                          varchar(255),
  contact_number                varchar(255),
  contact_name                  varchar(255),
  contact_address               varchar(255),
  license_number                varchar(255),
  license_img                   varchar(255),
  license_thumb_img             varchar(255),
  description                   varchar(255),
  approve_note                  varchar(255),
  log                           varchar(255),
  creator_id                    bigint not null,
  approver_id                   bigint not null,
  update_time                   bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_org primary key (id)
);

create table v1_system_config (
  id                            integer auto_increment not null,
  config_key                    varchar(255),
  config_value                  varchar(255),
  note                          varchar(255),
  update_time                   bigint not null,
  constraint pk_v1_system_config primary key (id)
);

create table v1_product (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  category_id                   bigint not null,
  brand_id                      bigint not null,
  wine_id                       bigint not null,
  org_id                        bigint not null,
  mail_fee_id                   bigint not null,
  type_id                       bigint not null,
  sketch                        varchar(255),
  details                       varchar(255),
  keywords                      varchar(255),
  tag                           varchar(255),
  marque                        varchar(255),
  barcode                       varchar(255),
  virtual_count                 bigint not null,
  price                         double not null,
  wholesale_price               double not null,
  cost_price                    double not null,
  market_price                  double not null,
  max_score_used                integer not null,
  stock                         bigint not null,
  sold_amount                   bigint not null,
  warning_stock                 bigint not null,
  cover_img_url                 varchar(255),
  poster                        varchar(255),
  mix_code                      varchar(255),
  status                        integer not null,
  state                         integer not null,
  is_combo                      tinyint(1) default 0 not null,
  allow_use_score               tinyint(1) default 0 not null,
  sort                          integer not null,
  unit                          varchar(255),
  deleted_at                    bigint not null,
  update_time                   bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_product primary key (id)
);

create table v1_product_attr (
  id                            bigint auto_increment not null,
  product_id                    bigint not null,
  name                          varchar(255),
  sort                          integer not null,
  constraint pk_v1_product_attr primary key (id)
);

create table v1_product_attr_option (
  id                            bigint auto_increment not null,
  option_id                     bigint not null,
  name                          varchar(255),
  attr_id                       bigint not null,
  supplier_option_id            bigint not null,
  sort                          integer not null,
  constraint pk_v1_product_attr_option primary key (id)
);

create table v1_product_classify (
  id                            bigint auto_increment not null,
  classify_code                 varchar(255),
  sort                          bigint not null,
  product_count                 integer not null,
  constraint pk_v1_product_classify primary key (id)
);

create table v1_product_classify_detail (
  id                            bigint auto_increment not null,
  product_id                    bigint not null,
  classify_id                   bigint not null,
  sort                          bigint not null,
  constraint pk_v1_product_classify_detail primary key (id)
);

create table v1_product_concrete (
  id                            bigint auto_increment not null,
  product_id                    bigint not null,
  concrete_type                 integer not null,
  constraint pk_v1_product_concrete primary key (id)
);

create table v1_product_img (
  id                            bigint auto_increment not null,
  img_url                       varchar(255),
  product_id                    bigint not null,
  tips                          varchar(255),
  sort                          integer not null,
  update_time                   bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_product_img primary key (id)
);

create table v1_product_mix (
  id                            bigint auto_increment not null,
  product_id                    bigint not null,
  mix_code                      varchar(255),
  constraint pk_v1_product_mix primary key (id)
);

create table v1_product_param (
  id                            bigint auto_increment not null,
  product_id                    bigint not null,
  name                          varchar(255),
  value                         varchar(255),
  constraint pk_v1_product_param primary key (id)
);

create table v1_product_search (
  id                            bigint auto_increment not null,
  keyword                       varchar(255),
  views                         bigint not null,
  constraint pk_v1_product_search primary key (id)
);

create table v1_product_sku (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  product_id                    bigint not null,
  img_url                       varchar(255),
  price                         double not null,
  sold_amount                   bigint not null,
  stock                         bigint not null,
  sort                          integer not null,
  code                          varchar(255),
  barcode                       varchar(255),
  data                          varchar(255),
  constraint pk_v1_product_sku primary key (id)
);

create table v1_product_tab (
  id                            bigint auto_increment not null,
  tab_name                      varchar(255),
  sort                          integer not null,
  constraint pk_v1_product_tab primary key (id)
);

create table v1_product_tab_classify (
  id                            bigint auto_increment not null,
  product_tab_id                bigint not null,
  classify_id                   bigint not null,
  classify_cover_img_url        varchar(255),
  classify_name                 varchar(255),
  sort                          integer not null,
  constraint pk_v1_product_tab_classify primary key (id)
);

create table v1_product_tab_products (
  id                            bigint auto_increment not null,
  product_tab_id                bigint not null,
  product_id                    bigint not null,
  sort                          integer not null,
  constraint pk_v1_product_tab_products primary key (id)
);

create table v1_product_tag (
  id                            bigint auto_increment not null,
  product_id                    bigint not null,
  tag                           varchar(255),
  constraint pk_v1_product_tag primary key (id)
);

create table v1_promot (
  id                            bigint auto_increment not null,
  uid                           bigint not null,
  invite_count                  integer not null,
  income                        bigint not null,
  status                        integer not null,
  invite_code                   varchar(255),
  update_time                   bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_promot primary key (id)
);

create table v1_promot_member (
  id                            bigint auto_increment not null,
  promotion_id                  bigint not null,
  uid                           bigint not null,
  master_id                     bigint not null,
  is_award                      integer not null,
  create_time                   bigint not null,
  constraint pk_v1_promot_member primary key (id)
);

create table v1_region (
  id                            integer auto_increment not null,
  region_code                   varchar(255),
  region_name                   varchar(255),
  parent_id                     integer not null,
  region_level                  integer not null,
  region_order                  integer not null,
  region_name_en                varchar(255),
  region_short_name_en          varchar(255),
  constraint pk_v1_region primary key (id)
);

create table v1_shopping_cart (
  id                            bigint auto_increment not null,
  uid                           bigint not null,
  product_id                    bigint not null,
  sku_id                        bigint not null,
  amount                        bigint not null,
  update_time                   bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_shopping_cart primary key (id)
);

create table v1_product_special_topic (
  id                            bigint auto_increment not null,
  cover_img_url                 varchar(255),
  title                         varchar(255),
  details                       varchar(255),
  product_count                 integer not null,
  status                        integer not null,
  create_time                   bigint not null,
  constraint pk_v1_product_special_topic primary key (id)
);

create table v1_product_st_list (
  id                            bigint auto_increment not null,
  product_id                    bigint not null,
  topic_id                      bigint not null,
  constraint pk_v1_product_st_list primary key (id)
);

create table v1_system_attr (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  sys_cate_type_id              bigint not null,
  sort                          integer not null,
  constraint pk_v1_system_attr primary key (id)
);

create table v1_system_attr_option (
  id                            bigint auto_increment not null,
  attr_id                       bigint not null,
  option_id                     bigint not null,
  name                          varchar(255),
  sort                          integer not null,
  constraint pk_v1_system_attr_option primary key (id)
);

create table v1_system_carousel (
  id                            integer auto_increment not null,
  name                          varchar(255),
  img_url                       varchar(255),
  link_url                      varchar(255),
  mobile_img_url                varchar(255),
  mobile_link_url               varchar(255),
  type                          integer not null,
  sort                          integer not null,
  need_show                     tinyint(1) default 0 not null,
  title1                        varchar(255),
  title2                        varchar(255),
  note                          varchar(255),
  region_code                   varchar(255),
  region_name                   varchar(255),
  update_time                   bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_system_carousel primary key (id)
);

create table v1_system_category_type (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  attr_count                    integer not null,
  param_count                   integer not null,
  sort                          integer not null,
  constraint pk_v1_system_category_type primary key (id)
);

create table v1_system_link (
  id                            integer auto_increment not null,
  name                          varchar(255),
  url                           varchar(255),
  sort                          integer not null,
  status                        integer not null,
  note                          varchar(255),
  update_time                   bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_system_link primary key (id)
);

create table v1_system_param (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  sys_cate_type_id              bigint not null,
  method                        varchar(255),
  value                         varchar(255),
  sort                          integer not null,
  constraint pk_v1_system_param primary key (id)
);

create table v1_transaction (
  id                            bigint auto_increment not null,
  order_tx_id                   varchar(255),
  uid                           bigint not null,
  amount                        bigint not null,
  usedscore                     bigint not null,
  pay_method                    integer not null,
  pay_source                    varchar(255),
  status                        integer not null,
  complete_time                 bigint not null,
  note                          varchar(255),
  update_time                   bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_transaction primary key (id)
);

create table v1_transaction_log (
  id                            bigint auto_increment not null,
  order_tx_id                   varchar(255),
  events                        varchar(255),
  result                        varchar(255),
  update_time                   bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_transaction_log primary key (id)
);

create table v1_wine (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  name_en                       varchar(255),
  category_id                   varchar(255),
  details                       varchar(255),
  img_url                       varchar(255),
  style                         varchar(255),
  style_id                      bigint not null,
  product_id                    bigint not null,
  alcohol_percent               double not null,
  bitter_percent                double not null,
  brand_name                    varchar(255),
  production_place              varchar(255),
  status                        integer not null,
  wanna_count                   bigint not null,
  drunk_count                   bigint not null,
  comments                      bigint not null,
  smell_rate                    integer not null,
  taste_rate                    integer not null,
  shape_rate                    integer not null,
  feel_rate                     integer not null,
  sort                          integer not null,
  tag                           varchar(255),
  update_time                   bigint not null,
  create_time                   bigint not null,
  constraint pk_v1_wine primary key (id)
);


# --- !Downs

drop table if exists v1_balance_log;

drop table if exists v1_brand;

drop table if exists v1_category;

drop table if exists v1_category_attr;

drop table if exists v1_charge;

drop table if exists v1_comment;

drop table if exists v1_contact_detail;

drop table if exists v1_coupon_config;

drop table if exists v1_dealer;

drop table if exists v1_dict;

drop table if exists v1_flash_sale;

drop table if exists v1_flash_sale_product;

drop table if exists v1_mail_fee_config;

drop table if exists v1_member;

drop table if exists v1_member_balance;

drop table if exists v1_member_coupon;

drop table if exists v1_member_level;

drop table if exists v1_member_like;

drop table if exists v1_member_score_config;

drop table if exists v1_member_score_log;

drop table if exists v1_mix_option;

drop table if exists v1_operation_log;

drop table if exists v1_order;

drop table if exists v1_order_detail;

drop table if exists v1_org;

drop table if exists v1_system_config;

drop table if exists v1_product;

drop table if exists v1_product_attr;

drop table if exists v1_product_attr_option;

drop table if exists v1_product_classify;

drop table if exists v1_product_classify_detail;

drop table if exists v1_product_concrete;

drop table if exists v1_product_img;

drop table if exists v1_product_mix;

drop table if exists v1_product_param;

drop table if exists v1_product_search;

drop table if exists v1_product_sku;

drop table if exists v1_product_tab;

drop table if exists v1_product_tab_classify;

drop table if exists v1_product_tab_products;

drop table if exists v1_product_tag;

drop table if exists v1_promot;

drop table if exists v1_promot_member;

drop table if exists v1_region;

drop table if exists v1_shopping_cart;

drop table if exists v1_product_special_topic;

drop table if exists v1_product_st_list;

drop table if exists v1_system_attr;

drop table if exists v1_system_attr_option;

drop table if exists v1_system_carousel;

drop table if exists v1_system_category_type;

drop table if exists v1_system_link;

drop table if exists v1_system_param;

drop table if exists v1_transaction;

drop table if exists v1_transaction_log;

drop table if exists v1_wine;

