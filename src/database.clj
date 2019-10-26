(ns database
  (:refer-clojure :exclude [find])
  (:require
   [config]
   [next.jdbc :as jdbc]
   [next.jdbc.sql :as sql]
   [next.jdbc.result-set :as result-set]
   [ragtime.jdbc]
   [ragtime.repl]
   [tick.alpha.api :as t]
   [clojure.spec.alpha :as s]
   [specs]))

(def db (jdbc/get-datasource (:database config/config)))

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
  {:pre  [(s/valid? :db/bigserial id)]
   :post [(s/valid? (s/nilable (s/map-of keyword? any?)) %)]}
  (sql/get-by-id db table id {:builder-fn as-kebab-maps}))


(defn find-bookmark
  [db id]
  {:pre  [(s/valid? :bookmarks/id id)]
   :post [(s/valid? (s/nilable :bookmarks/bookmark) %)]}
  (find-by-id db :bookmarks id))

(defn create-bookmark
  [db values]
  (sql/insert! db :bookmarks values {:builder-fn as-kebab-maps}))

(s/fdef create-bookmark
  :args (s/cat :db :db/connectable
               :values (s/keys :req [:bookmarks/url :bookmarks/title]))
  :ret (s/nilable :bookmarks/bookmark))

;;(stest/check 'database/create-bookmark {:gen {:db/connectable #(gen/return database/db)}})
