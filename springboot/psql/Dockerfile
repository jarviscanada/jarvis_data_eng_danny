FROM postgres:9.6-alpine

ADD /sql_ddl/schema.sql /docker-entrypoint-initdb.d
COPY sql_ddl /docker-entrypoint-initdb.d
