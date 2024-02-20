create table if not exists admins (
    id integer primary key auto_increment,
    chat_id integer,
    username varchar(255) not null unique
);
