(ns config
  (:refer-clojure :exclude [load])
  (:require
   [aero.core :refer (read-config)]))

(def env
  (keyword (System/getProperty "app.env" "dev")))

(def repl?
  (= "true" (System/getProperty "app.repl")))

(defn load-config
  ([] (load-config env))
  ([env] (assoc (read-config (clojure.java.io/resource "config.edn") {:profile env}) :env env)))

(def config
  (load-config))

(prn config)

(defn load
  ([] (load-config env))
  ([env] (assoc (read-config (clojure.java.io/resource "config.edn") {:profile env}) :env env)))
