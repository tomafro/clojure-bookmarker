(ns util)

(defn to-base [radix n]
  (Integer/toString n radix))

(def base36 (partial to-base 36))
