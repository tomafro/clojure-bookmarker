(ns echo
  (:require
   [response]))

(defn echo
  [request]
  (response/ok (with-out-str (clojure.pprint/pprint request)) "Content-Type" "text/plain" "Tom" "hello"))

(defn routes
  []
  [["/echo" {:any [:echo `echo]}]])
