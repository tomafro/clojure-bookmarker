(ns echo
  (:require
   [response]
   [io.pedestal.interceptor.helpers :as interceptor]))

(def echo-context
  (interceptor/before
   ::echo-context
   (fn [context] (assoc context :response (response/ok (with-out-str (clojure.pprint/pprint context)))))))

(defn echo
  [request]
  (response/ok (with-out-str (clojure.pprint/pprint request))))

(defn routes
  []
  [["/echo" {:any [:echo `echo]}]
   ["/echo-context" {:any [:echo-context `echo-context]}]])
