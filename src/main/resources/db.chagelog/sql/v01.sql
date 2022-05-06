--liquibase formatted sql

--changeset Alexander:1
CREATE TABLE Documents
(
    id      bigint primary key,
    doc_name varchar(255),
    number  bigint
);
--rollback drop table Document
CREATE TABLE Person
(
    id         bigint primary key,
    first_name  varchar(255) not null,
    second_name varchar(255) not null,
    age        int,
    CONSTRAINT age CHECK (age > 0 AND age < 130),
    document_id bigint unique ,
    FOREIGN KEY (document_id)
        REFERENCES Documents(id) ON DELETE CASCADE
            ON UPDATE CASCADE
);

--rollback drop table Person

CREATE TABLE Hobbies
(
    id   bigint primary key,
    name varchar(255)
);
--rollback drop table Hobbies
CREATE TABLE Person_Hobbies
(
    person_id  bigint ,
    hobbies_id bigint ,
    FOREIGN KEY (person_id) REFERENCES Person(id),
    FOREIGN KEY (hobbies_id) REFERENCES Hobbies(id)
);
--rollback drop table Person_Hobbies