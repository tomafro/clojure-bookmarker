(ns database
  (:refer-clojure :exclude [find count])
  (:require
   [config]
   [next.jdbc :refer [get-datasource execute!]]
   [next.jdbc.sql :as sql]
   [next.jdbc.result-set :as result-set]
   [next.jdbc.middleware]
   [io.pedestal.log]
   [ragtime.jdbc]
   [ragtime.repl]
   [clojure.spec.alpha :as s]
   [specs]))

(defn before-sql
  [sql-p opts]
  [sql-p (merge opts {::start (System/nanoTime) ::sql sql-p})])

(defn after-sql
  [rs opts]
  (let [statement (first (::sql opts))
        elapsed (int (/ (- (System/nanoTime) (::start opts)) 1000000))]
    (io.pedestal.log/info :msg (format "'%s' in %dms" statement elapsed)))
  [rs opts])

(defn wrap-datasource
  [datasource]
  (next.jdbc.middleware/wrapper datasource
                                {:pre-execute-fn before-sql
                                 :post-execute-fn after-sql}))

(def ^:dynamic db (wrap-datasource (get-datasource (:database config/config))))

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
