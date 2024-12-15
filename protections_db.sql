CREATE DATABASE IF NOT EXISTS `protections_db` DEFAULT CHARACTER SET utf8 ;
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
  PRIMARY KEY (`id`, `id_block_coordinate`),
  INDEX `fk_protections_block_coordinate1_idx` (`id_block_coordinate` ASC) VISIBLE,
  CONSTRAINT `fk_protections_block_coordinate1`
    FOREIGN KEY (`id_block_coordinate`)
    REFERENCES `protections_db`.`block_coordinate` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- PROCEDURES -- PROTECTION
-- -----------------------------------------------------

DELIMITER $$
create procedure get_protections_in_use()
begin
	select p.id 'ID_PROTECTION', p.name 'NAME', p.owner 'OWNER', p.in_use 'IN_USE', p.owner_uuid 'OWNER_UUID', p.world 'WORLD',
	bl_c.x 'X', bl_c.y 'Y', bl_c.z 'Z', bl_c.date_the_block_was_placed 'DATE',
    bl_c.x_dimension 'X_DIMENSION', bl_c.z_dimension 'z_dimension'
    from protections p
    inner join block_coordinate bl_c on p.id_block_coordinate = bl_c.id
    where p.in_use = true;
end;$$

create procedure create_new_protection(
	pa_name varchar(20),
    pa_in_use tinyint,
    pa_owner varchar(18),
    pa_owner_uuid binary(16),
    pa_world varchar(255),
    pa_id_block_coordinate bigint
)
begin
	insert into protections(name, in_use, owner, owner_uuid, world, id_block_coordinate) values(pa_name, pa_in_use, pa_owner, pa_owner_uuid, pa_world, pa_id_block_coordinate);
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
DELIMITER ;
select * from protections;
select * from block_coordinate;