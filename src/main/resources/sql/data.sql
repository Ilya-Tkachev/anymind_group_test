DELETE FROM incoming;
INSERT INTO incoming (received, amount) VALUES ('2022-04-01 01:00:00', 1000);
DELETE FROM statistic;
INSERT INTO statistic (status_at, amount) VALUES ('2022-04-01 01:00:00', 1000);