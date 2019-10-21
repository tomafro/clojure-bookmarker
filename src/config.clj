(ns config
  (:require
   [aero.core :refer (read-config)]))

(def config
  (read-config (clojure.java.io/resource "config.edn")))

(prn config)
