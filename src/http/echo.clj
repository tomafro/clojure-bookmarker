(ns http.echo
  (:require
   [http.response :as response]
   [io.pedestal.interceptor.helpers :as interceptor]))

(def echo-context
  (interceptor/before
   ::echo-context
   (fn [context] (assoc context :response (response/ok (with-out-str (clojure.pprint/pprint context)))))))

(defn echo
  [request]
  (response/ok (with-out-str (clojure.pprint/pprint request))))

(defn echo-header
  [request]
  (let [name (get-in request [:path-params :header])
        header (get-in request [:headers name])]
    (response/ok header)))


(defn routes
  []
  [["/echo/header/:header" {:any [:echo-header `echo-header]}]
   ["/echo" {:any [:echo `echo]}]
   ["/echo-context" {:any [:echo-context `echo-context]}]])
