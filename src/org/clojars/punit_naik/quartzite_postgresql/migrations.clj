(ns org.clojars.punit-naik.quartzite-postgresql.migrations
  (:require
   [environ.core :as environ]
   [migratus.core :as migratus]
   [taoensso.timbre :as log]))

(defn db-config
  []
  {:dbtype   "postgresql"
   :dbname   (environ/env :migratus-db-name)
   :host     (environ/env :migratus-db-host)
   :port     (environ/env :migratus-db-port)
   :user     (environ/env :migratus-db-user)
   :password (environ/env :migratus-db-password)})

(defn migratus-config
  []
  {:store                :database
   :migration-dir        "migrations/"
   :init-script          "init.sql"
   :init-in-transaction? true
   :migration-table-name "migrations"
   :db (db-config)})

(defn init!
  []
  (log/info
   "MIGRATIONS INIT COMPLETE:"
   (migratus/init
    (migratus-config))))