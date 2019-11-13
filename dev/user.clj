(ns user
 (:require
  [clojure.tools.namespace.repl]))

(def refresh
  clojure.tools.namespace.repl/refresh)

(defn repl
  []
  (require 'repl)
  (in-ns 'repl))
