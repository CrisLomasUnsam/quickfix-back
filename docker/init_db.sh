set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    CREATE DATABASE quickfix;
    CREATE DATABASE quickfix_test;
    CREATE USER docker WITH ENCRYPTED PASSWORD 'docker';
    GRANT ALL PRIVILEGES ON DATABASE quickfix TO docker;
    GRANT ALL PRIVILEGES ON DATABASE quickfix_test TO docker;
EOSQL