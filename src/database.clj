(ns database
  (:refer-clojure :exclude [find count])
  (:require
   [config]
   [next.jdbc :refer [get-datasource execute!]]
   [next.jdbc.sql :as sql]
   [next.jdbc.result-set :as result-set]
   [ragtime.jdbc]
   [ragtime.repl]
   [clojure.spec.alpha :as s]
   [specs]))

(def ^:dynamic db (get-datasource (:database config/config)))

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

(defn find-by-id
  [db table id]
  (sql/get-by-id db table id {:builder-fn as-kebab-maps}))

(s/fdef find-by-id
  :args (s/cat :id :db/bigserial)
  :ret (s/nilable (s/map-of keyword? any?)))

(defn find-first
  [db table]
  (first (sql/query db [(str "SELECT * from " (name table) " ORDER BY id ASC")] {:builder-fn as-kebab-maps})))

(defn count
  [db table]
  (:count (first (sql/query db [(str "SELECT COUNT(*) count FROM " (name table))]))))

(s/fdef count
  :ret  :postgres/bigint)

(defprotocol Repository
  (find [this id]))

(deftype DatabaseRepository [connection table]
         Repository
  (find [this id] sql/get-by-id connection table id {:builder-fn as-kebab-maps}))
