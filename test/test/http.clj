(ns test.http
  (:refer-clojure :exclude [get select])
  (:require [io.pedestal.test :refer [response-for]]
            [next.jdbc :as jdbc]
            [http :as server]
            [routes :refer [url-for]]
            [io.pedestal.http :as http]
            [database]))

(defn run-within-transaction [test]
  (jdbc/with-transaction [tx database/db {:rollback-only true}]
    (binding [database/db tx]
      (test))))

(def service
  (::http/service-fn (http/create-servlet server/service-map)))

(def get
  (partial response-for service :get))

(def post
  (partial response-for service :post))

(defn get-url [url]
  (response-for service :get (url-for url)))
