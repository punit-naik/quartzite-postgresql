(ns org.clojars.punit-naik.quartzite-postgresql.migrations-test
  (:require
   [migratus.core :as migratus]
   [org.clojars.punit-naik.quartzite-postgresql.migrations :as migrations]
   [taoensso.timbre :as log]))

(defn migratus-config
  []
  {:store                :database
   :migration-dir        "migrations_test/"
   :init-script          "destroy.sql"
   :init-in-transaction? true
   :migration-table-name "migrations"
   :db (migrations/db-config)})

(defn destroy!
  []
  (log/info
   "MIGRATIONS DESTROY COMPLETE:"
   (migratus/init
    (migratus-config))))