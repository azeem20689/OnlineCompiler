#!/bin/bash
if [ "$EUID" -ne 0 ]; then
    echo "Please run this script as root."
    exit 1
fi

# Input parameters
NEW_USERNAME=$1
PASSWORD=$2

# Create a new user
useradd -m -s /bin/bash "$NEW_USERNAME"

# Set the password for the new user
echo "$NEW_USERNAME:$PASSWORD" | chpasswd

# Give the new user sudo privileges
#usermod -aG sudo "$NEW_USERNAME"

echo "User  has been created with password "

