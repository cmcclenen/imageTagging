create sequence hibernate_sequence start 1 increment 1;

    create table image_tag (
       image_id int4 not null,
        tag_name varchar(255) not null,
        primary key (image_id, tag_name)
    );

    create table image_metadata (
       id int4 not null,
        image_data bytea,
        label varchar(255),
        tagging_enabled boolean not null,
        primary key (id)
    );

    create table tag (
       tag_name varchar(255) not null,
        primary key (tag_name)
    );

    alter table image_tag 
       add constraint FKmnik5m38pxxwa63g9t6mwi685 
       foreign key (tag_name) 
       references tag;

    alter table image_tag 
       add constraint FK4lgdgdou8t0rt8mp9ow7olbbi 
       foreign key (image_id) 
       references image_metadata;
