#!/bin/bash

./manage-test-postgresql-db.sh start
lein with-profiles +dev test
./manage-test-postgresql-db.sh stop