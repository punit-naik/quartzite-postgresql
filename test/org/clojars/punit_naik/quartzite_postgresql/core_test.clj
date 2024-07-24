(ns org.clojars.punit-naik.quartzite-postgresql.core-test
  (:import
   clojure.lang.Keyword)
  (:require
   [clojure.test :refer [deftest is testing use-fixtures]]
   [clojurewerkz.quartzite.jobs :as j]
   [clojurewerkz.quartzite.scheduler :as qs]
   [clojurewerkz.quartzite.schedule.simple :as ss]
   [clojurewerkz.quartzite.triggers :as t]
   [honey.sql :as honey]
   [next.jdbc :as jdbc]
   [next.jdbc.result-set :as rs]
   [org.clojars.punit-naik.quartzite-postgresql.core :as target]
   [org.clojars.punit-naik.quartzite-postgresql.migrations :as migrations]
   [org.clojars.punit-naik.quartzite-postgresql.migrations-test :as migrations-test]
   [pg-embedded-clj.core :as pe]
   [taoensso.timbre :as log]))

(def sc (atom nil))

(def connection (atom nil))

(defn scheduler-fixture
  [f]
  (with-open [c (-> (migrations/db-config) jdbc/get-datasource jdbc/get-connection)]
    (migrations/init!)
    (let [sch (qs/start (target/make-scheduler!))]
      (reset! sc sch)
      (reset! connection c)
      (f)
      (qs/shutdown sch)
      (reset! sc nil)
      (reset! connection nil)))
  (migrations-test/destroy!))

(use-fixtures :each scheduler-fixture)

(defn ->format-options [{:keys [^Boolean pretty?]}]
  (cond-> nil
    (true? pretty?)
    (assoc :pretty true)))

(defn ->builder-fn [^Keyword builder]
  (case builder
    :as-unqualified-kebab-maps
    rs/as-unqualified-kebab-maps

    :as-unqualified-maps
    rs/as-unqualified-maps

    ;; else
    (throw (new Exception "Unsupported row builder"))))

(defn ->jdbc-options
  [{:keys [^Keyword builder]}]
  (cond-> {:builder-fn rs/as-unqualified-kebab-maps}
    (some? builder)
    (assoc :builder-fn (->builder-fn builder))))

(defn execute!
  {:arglists
   '([db query-map]
     [db query-map {:keys [pretty? builder]}])}

  ([db query-map]
   (execute! db query-map nil))

  ([db query-map options]
   (jdbc/execute!
    db
    (honey/format query-map (->format-options options))
    (->jdbc-options options))))

#_{:clj-kondo/ignore [:unresolved-symbol]}
(j/defjob TestJob
  [ctx]
  (log/info
   "EXECUTING JOB:"
   (->> {:insert-into :test
         :values [{:id "executed"}]}
        (execute! @connection))))

(defn create-test-table!
  []
  (jdbc/execute!
   @connection
   ["CREATE TABLE IF NOT EXISTS test (id TEXT)"]))

(defn delete-test-table!
  []
  (jdbc/execute!
   @connection
   ["DELETE FROM test WHERE TRUE"])
  (jdbc/execute!
   @connection
   ["TRUNCATE TABLE test"])
  (jdbc/execute!
   @connection
   ["DROP TABLE IF EXISTS test"]))

(deftest scheduler-test
  (testing "If the scheduler is working properly and is persisting job data in the DB"
    (create-test-table!)
    (let [sc @sc
          jk-str "jobs.noop.1"
          jk (j/key jk-str)
          job (j/build
               #_{:clj-kondo/ignore [:invalid-arity]}
               (j/of-type #_{:clj-kondo/ignore [:unresolved-symbol]} TestJob)
               #_{:clj-kondo/ignore [:invalid-arity]}
               (j/with-identity jk))
          tk-str "triggers.1"
          tk (t/key tk-str)
          trigger (t/build
                   #_{:clj-kondo/ignore [:invalid-arity]}
                   (t/with-identity tk)
                   #_{:clj-kondo/ignore [:invalid-arity]}
                   (t/start-now)
                   #_{:clj-kondo/ignore [:invalid-arity]}
                   (t/with-schedule
                     (ss/schedule
                      (ss/with-repeat-count 0)
                      (ss/with-interval-in-milliseconds 1))))]
      (log/info "JOBS SCHEDULED:" (qs/schedule sc job trigger))
      (is (= [{:job-name jk-str}]
             (->> {:select :job-name
                   :from :qrtz-job-details}
                  (execute! @connection))))
      (is (= [{:trigger-name tk-str}]
             (->> {:select :trigger-name
                   :from :qrtz-triggers}
                  (execute! @connection))))
      ;; Apparently we have to sleep for a bit for the job to execute
      ;; Maybe some problems with the fixture/migrations
      (Thread/sleep 100)
      ;;
      (is (= [{:id "executed"}]
             (->> {:select :id
                   :from :test}
                  (execute! @connection))))
      (qs/delete-trigger sc tk)
      (qs/delete-job sc jk))
    (delete-test-table!)))
