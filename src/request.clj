(ns request
  (:require [medley.core :refer [map-keys]]))

(def normalize-header-name (comp clojure.string/lower-case name))

(defn get-headers
  [request names]
  (let [names (map normalize-header-name names)
        headers (map-keys normalize-header-name (:headers request))]
    (map headers names)))
 
(defn get-header
  [request name]
  (first (get-headers request [name])))
