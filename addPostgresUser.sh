#!/bin/bash

# Set up PostgreSQL connection details
PGHOST=`hostname -I | cut -d " " -f1`
PGPORT="5432"
PGDATABASE="postgres"
PGUSER="postgres"
PGPASSWORD=""

# Function to create a new user and set up their schema
create_user_and_schema() {
    local username="$1"
    local password="$2"

    # Create user
    psql -h "$PGHOST" -p "$PGPORT" -d "$PGDATABASE" -U "$PGUSER" -c "CREATE USER $username WITH PASSWORD '$password';"

    # Create schema for the user
    psql -h "$PGHOST" -p "$PGPORT" -d "$PGDATABASE" -U "$PGUSER" -c "CREATE SCHEMA ${username}_schema AUTHORIZATION $username;"

    # Grant usage and create privileges on the schema
    psql -h "$PGHOST" -p "$PGPORT" -d "$PGDATABASE" -U "$PGUSER" -c "GRANT USAGE, CREATE ON SCHEMA ${username}_schema TO $username;"

    # Set default schema for the user
    psql -h "$PGHOST" -p "$PGPORT" -d "$PGDATABASE" -U "$PGUSER" -c "ALTER USER $username SET search_path TO ${username}_schema, public;"
    psql -h "$PGHOST" -p "$PGPORT" -d "$PGDATABASE" -U "$PGUSER" -c "SET search_path TO ${username}_schema"
	
}


create_user_and_schema $1 $2


