insert into wallet(id,customer_id,balance) values (1,1,100);
insert into wallet(id,customer_id,balance) values (2,2,100);
insert into wallet(id,customer_id,balance) values (3,3,100);
insert into wallet(id,customer_id,balance) values (4,4,100);

insert into transaction(id,wallet_id,type,description,amount) values (1,1,'CREDIT','CARD PAYMENT',150);
insert into transaction(id,wallet_id,type,description,amount) values (2,1,'DEBIT','CARD PAYMENT',50);
insert into transaction(id,wallet_id,type,description,amount) values (3,2,'CREDIT','CARD PAYMENT',120);
insert into transaction(id,wallet_id,type,description,amount) values (4,2,'DEBIT','CARD PAYMENT',20);
insert into transaction(id,wallet_id,type,description,amount) values (5,3,'CREDIT','CARD PAYMENT',80);
insert into transaction(id,wallet_id,type,description,amount) values (6,3,'CREDIT','CARD PAYMENT',20);
insert into transaction(id,wallet_id,type,description,amount) values (7,4,'CREDIT','CARD PAYMENT',50);
insert into transaction(id,wallet_id,type,description,amount) values (8,4,'CREDIT','CARD PAYMENT',50);


