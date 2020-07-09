CREATE DATABASE trading_dev;
GRANT ALL PRIVILEGES ON DATABASE trading_dev TO postgres;
\c trading_dev
\i /docker-entrypoint-initdb.d/schema.sql
--db for test case
CREATE DATABASE trading_test;
GRANT ALL PRIVILEGES ON DATABASE trading_test TO postgres;
\c trading_test
\i /docker-entrypoint-initdb.d/schema.sql

