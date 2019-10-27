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

(def ^:dynamic db (jdbc/get-datasource (:database config/config)))

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

(s/fdef find-bookmark
  :args (s/cat :id :db/bigserial)
  :ret (s/nilable (s/map-of keyword? any?)))

(defn find-bookmark
  [id]
  (find-by-id database/db :bookmarks id))

(s/fdef find-bookmark
  :args (s/cat :id :bookmarks/id)
  :ret (s/nilable :bookmarks/bookmark))

(defn create-bookmark
  [values]
  (sql/insert! database/db :bookmarks values {:builder-fn as-kebab-maps}))

(s/fdef create-bookmark
  :args (s/cat :values (s/keys :req [:bookmarks/url :bookmarks/title]))
  :ret :bookmarks/bookmark)

(defn count-bookmarks
  []
  (:count (first (sql/query database/db ["SELECT COUNT(*) FROM bookmarks"]))))

(s/fdef count-bookmarks
  :ret  :postgres/bigint)


;;(stest/check 'database/create-bookmark {:gen {:db/connectable #(gen/return database/db)}})
