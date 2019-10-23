(ns config
  (:require
   [aero.core :refer (read-config)]))

(def env
  (keyword (or (System/getenv "ENV") :dev)))

(def config
  (assoc (read-config (clojure.java.io/resource "config.edn") {:profile env}) :env env))

(prn config)
