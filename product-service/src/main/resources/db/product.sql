CREATE TABLE IF NOT EXISTS product (
                                       id BIGSERIAL PRIMARY KEY,
                                       name VARCHAR(255) NOT NULL,
                                       compound TEXT,
                                       price DOUBLE PRECISION NOT NULL
);

insert into product (id, name, compound, price)
values (1, 'Grylyazh', 'Nuts, chocolate', 2.5),
       (2,'Alenka', 'Milk, chocolate', 2),
       (3, 'Snickers', 'Nuts, chocolate, nougat', 5.3),
       (4,'Chernomorochka', 'Chocolate, jelly', 1.1);
