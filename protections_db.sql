CREATE SCHEMA IF NOT EXISTS `protections_db` DEFAULT CHARACTER SET utf8 ;
USE `protections_db` ;

-- -----------------------------------------------------
-- Table `protections_db`.`block_coordinate`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `protections_db`.`block_coordinate` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `x` DOUBLE NOT NULL,
  `y` DOUBLE NOT NULL,
  `z` DOUBLE NOT NULL,
  `x_dimension` DOUBLE NOT NULL,
  `z_dimension` DOUBLE NOT NULL,
  `date_the_block_was_placed` DATETIME NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `protections_db`.`flags`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `protections_db`.`flags` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `damage_mobs` TINYINT NOT NULL,
  `mob_spawning` TINYINT NOT NULL,
  `block_break` TINYINT NOT NULL,
  `block_place` TINYINT NOT NULL,
  `ender_pearl` TINYINT NOT NULL,
  `item_drop` TINYINT NOT NULL,
  `item_pickup` TINYINT NOT NULL,
  `leaf_decay` TINYINT NOT NULL,
  `explosion` TINYINT NOT NULL,
  `pvp` TINYINT NOT NULL,
  `tnt` TINYINT NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `protections_db`.`mena_information`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `protections_db`.`mena_information` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `name_to_give` VARCHAR(255) NOT NULL,
  `material` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `protections_db`.`protections`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `protections_db`.`protections` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(20) NOT NULL,
  `in_use` TINYINT NOT NULL,
  `owner` VARCHAR(18) NOT NULL,
  `owner_uuid` BINARY(16) NOT NULL,
  `world` VARCHAR(255) NOT NULL,
  `id_block_coordinate` BIGINT NOT NULL,
  `id_flags` BIGINT NOT NULL,
  `id_mena_information` BIGINT NOT NULL,
  PRIMARY KEY (`id`, `id_block_coordinate`, `id_flags`, `id_mena_information`),
  INDEX `fk_protections_block_coordinate1_idx` (`id_block_coordinate` ASC) VISIBLE,
  INDEX `fk_protections_flags1_idx` (`id_flags` ASC) VISIBLE,
  INDEX `fk_protections_mena_information1_idx` (`id_mena_information` ASC) VISIBLE,
  CONSTRAINT `fk_protections_block_coordinate1`
    FOREIGN KEY (`id_block_coordinate`)
    REFERENCES `protections_db`.`block_coordinate` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_protections_flags1`
    FOREIGN KEY (`id_flags`)
    REFERENCES `protections_db`.`flags` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_protections_mena_information1`
    FOREIGN KEY (`id_mena_information`)
    REFERENCES `protections_db`.`mena_information` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `protections_db`.`protection_members`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `protections_db`.`protection_members` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(18) NOT NULL,
  `member_uuid` BINARY(16) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `protections_db`.`protections_has_protection_members`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `protections_db`.`protections_has_protection_members` (
  `protections_id` BIGINT NOT NULL,
  `protections_id_block_coordinate` BIGINT NOT NULL,
  `protections_flags_id` BIGINT NOT NULL,
  `protection_members_id` BIGINT NOT NULL,
  PRIMARY KEY (`protections_id`, `protections_id_block_coordinate`, `protections_flags_id`, `protection_members_id`),
  INDEX `fk_protections_has_protection_members_protection_members1_idx` (`protection_members_id` ASC) VISIBLE,
  INDEX `fk_protections_has_protection_members_protections1_idx` (`protections_id` ASC, `protections_id_block_coordinate` ASC, `protections_flags_id` ASC) VISIBLE,
  CONSTRAINT `fk_protections_has_protection_members_protections1`
    FOREIGN KEY (`protections_id` , `protections_id_block_coordinate` , `protections_flags_id`)
    REFERENCES `protections_db`.`protections` (`id` , `id_block_coordinate` , `id_flags`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_protections_has_protection_members_protection_members1`
    FOREIGN KEY (`protection_members_id`)
    REFERENCES `protections_db`.`protection_members` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- PROCEDURES -- PROTECTION
-- -----------------------------------------------------

DELIMITER $$
create procedure get_protections_in_use()
begin
	select 
    /* --- Protections ---*/
    p.id 'ID_PROTECTION', p.name 'NAME', p.owner 'OWNER', p.in_use 'IN_USE', p.owner_uuid 'OWNER_UUID', p.world 'WORLD',
    /* --- Block_Coordinates ---*/
    bl_c.id 'ID_BLOCK_COORDINATES',
	bl_c.x 'X', bl_c.y 'Y', bl_c.z 'Z', bl_c.date_the_block_was_placed 'DATE',
    bl_c.x_dimension 'X_DIMENSION', bl_c.z_dimension 'Z_DIMENSION',
    /* --- Flags ---*/
    f.id 'ID_FLAGS', f.damage_mobs 'DAMAGE_MOBS', f.mob_spawning 'MOB_SPAWNING', f.block_break 'BLOCK_BREAK', 
    f.block_place 'BLOCK_PLACE', 
    f.ender_pearl 'ENDER_PEARL', f.item_drop 'ITEM_DROP', f.item_pickup 'ITEM_PICKUP',
    f.leaf_decay 'LEAF_DECAY', f.explosion 'EXPLOSION', f.pvp 'PVP', f.tnt 'TNT',
    /* --- Mena_Information ---*/
    mi.name 'MENA_NAME', mi.name_to_give 'MENA_NAME_TO_GIVE', mi.material 'MENA_MATERIAL'
    from protections p
    inner join block_coordinate bl_c on p.id_block_coordinate = bl_c.id
    inner join flags f on p.id_flags = f.id
    inner join mena_information mi on p.id_mena_information = mi.id
    where p.in_use = true;
end;$$

create procedure create_new_protection(
	pa_name varchar(20),
    pa_in_use tinyint,
    pa_owner varchar(18),
    pa_owner_uuid binary(16),
    pa_world varchar(255),
    pa_id_block_coordinate bigint,
    pa_id_flags bigint,
    pa_id_mena_information bigint
)
begin
	insert into protections(name, in_use, owner, owner_uuid, world, id_block_coordinate, id_flags, id_mena_information) values(pa_name, pa_in_use, pa_owner, pa_owner_uuid, pa_world, pa_id_block_coordinate, pa_id_flags, pa_id_mena_information);
end; $$

create procedure change_state_protection(
	pa_id bigint,
    pa_state tinyint
)
begin
	update protections p
    set in_use = pa_state
    where p.id = pa_id;
end;$$


create procedure change_owner_protection(
	pa_id bigint,
    pa_name varchar(20),
    pa_owner varchar(18),
    pa_owner_uuid binary(16)
)
begin
	update protections p
    set
    name = pa_name,
    owner = pa_owner,
    owner_uuid = pa_owner_uuid
    where p.id = pa_id;
end;$$

create procedure get_user_number_homes()
begin
	select p.owner_uuid 'OWNER_UUID' ,count(p.id) 'NUMBER_OF_HOMES' from protections p where p.in_use = true
    group by p.owner_uuid;
end;$$

-- -----------------------------------------------------
-- PROCEDURES -- COORDINATES
-- -----------------------------------------------------

create procedure create_new_block_coordinate(
	pa_x double,
    pa_y double,
    pa_z double,
    pa_x_dimension double,
    pa_z_dimension double,
    pa_date_the_block_was_placed datetime
)
begin
	insert into block_coordinate(x, y, z, x_dimension, z_dimension, date_the_block_was_placed) 
    values(pa_x, pa_y, pa_z, pa_x_dimension, pa_z_dimension, pa_date_the_block_was_placed);
end;$$

create procedure get_id_from_block_coordinate_with_coordinate(
	pa_x double,
    pa_y double,
    pa_z double
)
begin
	select id 'ID'
    from block_coordinate bl_c where bl_c.x = pa_x and bl_c.y = pa_y and bl_c.z = pa_z
    order by bl_c.date_the_block_was_placed desc
    limit 1;
end;$$

create procedure get_id_from_protections_with_block_coordinate_id(
	pa_id_block_coordinate int
)
begin
	select p.id 'ID' from protections p
    where p.id_block_coordinate = pa_id_block_coordinate
    limit 1;
end$$

-- -----------------------------------------------------
-- PROCEDURES -- FLAGS
-- -----------------------------------------------------

create function create_flags_and_get_id(
	pa_damage_mobs boolean,
    pa_mob_spawning boolean,
    pa_block_break boolean,
    pa_block_place boolean,
    pa_ender_pearl boolean,
    pa_item_drop boolean,
    pa_item_pickup boolean,
    pa_leaf_decay boolean,
    pa_explosion boolean,
    pa_pvp boolean,
    pa_tnt boolean
)
returns int
deterministic
begin
	declare new_flag_id bigint;
    insert into flags(damage_mobs, mob_spawning, block_break, block_place, ender_pearl, item_drop, item_pickup, leaf_decay, explosion, pvp, tnt)
    values (pa_damage_mobs, pa_mob_spawning, pa_block_break, pa_block_place, pa_ender_pearl, pa_item_drop, pa_item_pickup, pa_leaf_decay, pa_explosion, pa_pvp, pa_tnt);
    
    set new_flag_id = LAST_INSERT_ID();
    return new_flag_id;
end;$$

-- -----------------------------------------------------
-- PROCEDURES -- MENA_INFORMATION
-- -----------------------------------------------------

create procedure create_mena_information(
    pa_name varchar(255),
    pa_name_to_give varchar(255),
    pa_material varchar(255)
)
begin
	insert into mena_information( name, name_to_give, material)
    values( pa_name, pa_name_to_give, pa_material);
end;$$

create procedure get_all_mena_information()
begin
	select mi.id 'id', mi.name 'name', mi.name_to_give 'name_to_give', mi.material 'material' from mena_information mi;
end;$$

-- -----------------------------------------------------
-- PROCEDURES -- PROTECTIONS_MEMBERS
-- -----------------------------------------------------

create procedure add_member_to_protection(
	pa_protections_id bigint,
    pa_protections_id_block_coordinate bigint,
    pa_protections_flags_id bigint,
    pa_protection_members_id bigint
)
begin
	insert into protections_has_protection_members(protections_id, protections_id_block_coordinate, protections_flags_id, protection_members_id)
    values(pa_protections_id, pa_protections_id_block_coordinate, pa_protections_flags_id, pa_protection_members_id);
end;$$

create procedure get_all_members_with_protection()
begin
	select 
    /* --- Protections ---*/
    p.id 'ID_PROTECTION',
    /* --- Protection_Member ---*/
    pm.id 'ID_PROTECTION_MEMBER', pm.name 'NAME_PROTECTION_MEMBER', pm.member_uuid 'UUID_PROTECTION_MEMBER'
    from protections_has_protection_members phpm
    inner join protections p on phpm.protections_id = p.id
    inner join protection_members pm on phpm.protection_members_id = pm.id
    where p.in_use = true
    order by phpm.protections_id desc;
end;$$

create function get_id_by_uuid(
	fc_uuid binary(16)
)
returns int
deterministic
begin
	declare result_id int; 	
    select pm.id into result_id from protection_members pm where pm.member_uuid = fc_uuid limit 1;
    return result_id;
end;$$

create function get_id_from_protections_by_coordinate_and_flags_id(
	pa_id_block_coordinate bigint,
    pa_id_flags bigint
)
returns bigint
deterministic
begin
	declare id_protection bigint;
    select p.id into id_protection from protections p 
    where p.id_block_coordinate = pa_id_block_coordinate and p.id_flags = pa_id_flags
    limit 1;
    return id_protection;
end;$$

create procedure remove_member_from_protection(
	pa_protections_id bigint,
    pa_uuid_protections_members binary(16)
)
begin
	declare id int;
    set id = get_id_by_uuid(pa_uuid_protections_members);
	delete from protections_has_protection_members phpm
    where phpm.protections_id = pa_protections_id and phpm.protection_members_id = id;
end;$$

create procedure get_all_members()
begin
	select pm.name 'NAME_PROTECTION_MEMBER', pm.member_uuid 'UUID_PROTECTION_MEMBER' from protection_members pm;
end;$$

create procedure add_member(
	pa_name varchar(18),
    pa_uuid binary(16)
)
begin
	insert into protection_members(name, member_uuid)
    values(pa_name, pa_uuid);
end;$$
DELIMITER ;

use protections_db;

select * from protections;
select * from block_coordinate;
select * from flags;
select * from mena_information;
select * from protection_members;
call get_all_members();
call get_user_number_homes();
select get_id_from_protections_by_coordinate_and_flags_id(1, 1);
/**drop database protections_db;