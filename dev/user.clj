(ns user
 (:require
  [main]
  [clojure.spec.alpha :as s]
  [clojure.spec.gen.alpha :as gen]
  [clojure.tools.namespace.repl :refer [refresh]]))

(def start-dev main/start-dev)
