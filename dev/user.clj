(ns user
 (:require
  [clojure.spec.alpha :as s]
  [clojure.spec.gen.alpha :as gen]
  [clojure.tools.namespace.repl]))

(def refresh
  clojure.tools.namespace.repl/refresh)

(defn repl
  []
  (require 'repl)
  (in-ns 'repl))
