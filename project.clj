(defproject org.clojars.punit-naik/quartzite-postgresql "1.0.0"
  :description "A small helper library to set up Quartzite scheduler with PostgreSQL as a store"
  :url "http://github.com/punit-naik/quartzite-postgresql"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[clojurewerkz/quartzite "2.2.0"]
                 [environ "1.2.0"]
                 [migratus "1.5.4"]
                 [org.clojure/clojure "1.11.3"]
                 [org.postgresql/postgresql "42.7.1"]
                 [com.taoensso/timbre "6.5.0"]]
  :plugins [[lein-environ "1.2.0"]]
  :profiles
  {:dev
   {:env
    {:migratus-db-name     "test"
     :migratus-db-host     "localhost"
     :migratus-db-port     "5432"
     :migratus-db-user     "test"
     :migratus-db-password "test"}
    :resource-paths ["test-resources"]}
   :test
   {:dependencies [[com.github.seancorfield/honeysql "2.6.1147"]
                   [com.github.seancorfield/next.jdbc "1.3.939"]]}})