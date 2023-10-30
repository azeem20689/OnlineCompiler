#!/bin/bash

# Set up MySQL connection details
MYSQLHOST="localhost"
MYSQLPORT="3306"
MYSQLDATABASE="mysql"
MYSQLUSER="root"
MYSQLPASSWORD="Codecompass@123"

# Function to create a new user and set up their schema
create_user_and_schema() {
    local username="$1"
    local password="$2"

mysql -h "$MYSQLHOST" -P "$MYSQLPORT" -u "$MYSQLUSER" -p"$MYSQLPASSWORD" -e "SET GLOBAL validate_password.policy = 0;"

    # Create user
    mysql -h "$MYSQLHOST" -P "$MYSQLPORT" -u "$MYSQLUSER" -p"$MYSQLPASSWORD" -e "CREATE USER '$username'@'localhost' IDENTIFIED BY '$password';"

    # Create schema for the user
    mysql -h "$MYSQLHOST" -P "$MYSQLPORT" -u "$MYSQLUSER" -p"$MYSQLPASSWORD" -e "CREATE SCHEMA ${username}_schema;"

    # Grant privileges only on the user's schema
    mysql -h "$MYSQLHOST" -P "$MYSQLPORT" -u "$MYSQLUSER" -p"$MYSQLPASSWORD" -e "GRANT ALL PRIVILEGES ON ${username}_schema.* TO '$username'@'localhost';"
    
    # Revoke all privileges on other schemas
    mysql -h "$MYSQLHOST" -P "$MYSQLPORT" -u "$MYSQLUSER" -p"$MYSQLPASSWORD" -e "REVOKE ALL PRIVILEGES ON *.* FROM '$username'@'localhost';"
}

create_user_and_schema "$1" "$2"

