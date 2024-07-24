#!/bin/bash

./manage-test-postgresql-db.sh start 5434
lein with-profiles +dev test
./manage-test-postgresql-db.sh stop