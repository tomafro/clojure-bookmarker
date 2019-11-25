(ns database
  (:refer-clojure :exclude [find count])
  (:require
   [config]
   [next.jdbc :refer [get-datasource execute!]]
   [next.jdbc.sql :as sql]
   [next.jdbc.result-set :as result-set]
   [next.jdbc.middleware]
   [ragtime.jdbc]
   [ragtime.repl]
   [clojure.spec.alpha :as s]
   [specs]))


(defn log-sql
  [sql-p options]
  (prn sql-p)
  [sql-p options])

(defn get-unwrapped-db [] (get-datasource (:database config/config)))


;; (format "hello %d" (int (/ 223234 1000)))
(defn get-db
  []
  (let [start-fn (fn [sql-p opts]
                   (prn sql-p)
                   [sql-p (merge opts {::start (System/nanoTime) ::sql sql-p})])
                   ;;[sql-p opts])
        end-fn   (fn [rs opts]
                     (prn [(first (::sql opts)) (- (System/nanoTime) (::start opts))])
                     [rs opts])]
  (next.jdbc.middleware/wrapper (get-datasource (:database config/config))
                                {:pre-execute-fn start-fn
                                 :post-execute-fn end-fn})))

(def ^:dynamic db (get-db))

(defn ragtime-config
  ([] (ragtime-config config/env))
  ([env]
   {:datastore  (ragtime.jdbc/sql-database (:database (config/load-config env)))
    :migrations (ragtime.jdbc/load-resources "migrations")}))

(defn migrate
  ([] (migrate config/env))
  ([env]
   (ragtime.repl/migrate (ragtime-config env))))

(defn rollback
  ([] (rollback config/env))
  ([env]
   (ragtime.repl/rollback (ragtime-config env))))

(defn as-kebab-maps [rs opts]
  (let [kebab #(clojure.string/replace % #"_" "-")]
    (result-set/as-modified-maps rs (assoc opts :qualifier-fn kebab :label-fn kebab))))

(defn log-sql
  [sql-p _]
  (prn sql-p))

(def jdbc-options
  {:builder-fn database/as-kebab-maps
   :pre-execute-fn log-sql})

(defn find-by-id
  [db table id]
  (sql/get-by-id db table id jdbc-options))

(s/fdef find-by-id
  :args (s/cat :id :db/bigserial)
  :ret (s/nilable (s/map-of keyword? any?)))

(defn find-first
  [db table]
  (first (sql/query db [(str "SELECT * from " (name table) " ORDER BY id ASC")] jdbc-options)))

(defn count
  [db table]
  (:count (first (sql/query db [(str "SELECT COUNT(*) count FROM " (name table))] jdbc-options))))

(s/fdef count
  :ret  :postgres/bigint)

(defn create
  [db table values]
  (sql/insert! db table values jdbc-options))

(defn truncate
  [db table]
  (execute! db [(str "TRUNCATE " (name table))]))
