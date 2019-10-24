(ns config
  (:require
   [aero.core :refer (read-config)]))

(def env
  (keyword (or (System/getenv "ENV") :dev)))

(defn load-config
  ([] (load-config env))
  ([env] (assoc (read-config (clojure.java.io/resource "config.edn") {:profile env}) :env env)))

(def config
  (load-config))

(prn config)
