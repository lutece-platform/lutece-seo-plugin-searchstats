-- liquibase formatted sql
-- changeset searchstats:init_db_searchstats.sql
-- preconditions onFail:MARK_RAN onError:WARN
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM core_admin_role WHERE role_key = 'role_daemon_mail_recipient'
INSERT INTO core_admin_role (role_key,role_description)
VALUES ('role_daemon_mail_recipient','Role pour la liste de diffusion du daemon des statistiques de recherches.');

INSERT INTO core_user_role
VALUES ('role_daemon_mail_recipient',1);