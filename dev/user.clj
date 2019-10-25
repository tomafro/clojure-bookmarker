(ns user
 (:require
  [clojure.spec.alpha :as s]
  [clojure.spec.gen.alpha :as gen]
  [clojure.tools.namespace.repl]))

(def refresh
  clojure.tools.namespace.repl/refresh)

(defn repl
  []
  (in-ns 'repl))
