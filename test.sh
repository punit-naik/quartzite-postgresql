#!/bin/bash

./manage-test-postgresql-db.sh start 543211
lein with-profiles +dev test
./manage-test-postgresql-db.sh stop