DROP table inventory;

CREATE TABLE IF NOT EXISTS inventory (
                                         id BIGSERIAL PRIMARY KEY,
                                         inv_code VARCHAR(255),
                                         quantity INTEGER

);

insert into inventory (id, inv_code, quantity)
values (1, 'Grylyazh', 7),
       (2,'Alenka', 9),
       (3, 'Snickers', 14),
       (4,'Chernomorochka', 78);