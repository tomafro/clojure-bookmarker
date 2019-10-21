(ns database
  (:require
   [config]
   [next.jdbc :as jdbc]
   [ragtime.jdbc]
   [ragtime.repl]
   [honeysql.core :as sql]))

(def ds (jdbc/get-datasource (:database config/config)))

(defn ragtime-config
  []
  {:datastore  (ragtime.jdbc/sql-database (:database config/config))
   :migrations (ragtime.jdbc/load-resources "migrations")})

(defn migrate
  []
  (ragtime.repl/migrate (ragtime-config)))

(defn rollback
  []
  (ragtime.repl/rollback (ragtime-config)))

(defn select-one
  [sql]
  (jdbc/execute-one! ds (sql/format sql :namespace-as-table? true)))

(defn find-bookmark
  [id]
  (select-one
   (sql/build :select :* :from :bookmarks :where [:= :bookmarks/id id])))
