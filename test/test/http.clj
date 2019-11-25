(ns test.http
  (:refer-clojure :exclude [get select])
  (:require [io.pedestal.test :refer [response-for]]
            [bookmarks.db]
            [http.server :as server]
            [http.routes :refer [url-for]]
            [io.pedestal.http :as http]
            [database]
            [ring.util.codec]
            [clojure.string :refer [join]]
            [medley.core :refer [map-keys]]
            [io.aviso.repl]))

; (defn run-within-transaction [test]
;   (jdbc/with-transaction [tx database/db {:rollback-only true}]
;     (binding [database/db tx]
;       (test))))

;; Transactions aren't working with wrapped jdbc yet, so resorting to truncate for now
(defn run-within-transaction [test]
  (bookmarks.db/truncate)
  (test))


(def service
  (::http/service-fn (http/create-servlet server/service-map)))

(def get
  (partial response-for service :get))

(defn request-headers-from
  [options]
  (if (:form options)
    (assoc (:headers options) "Content-Type" "application/x-www-form-urlencoded")
    (:headers options)))

(defn form-encode
  [attributes]
  (let [attributes (map-keys #(join "/" (keep identity [(namespace %) (name %)])) attributes)]
    (ring.util.codec/form-encode attributes)))

(defn request-body-from
  [options]
  (if-let [form (:form options)]
    (form-encode form)
    (:body options)))

(defn url-from
  [url]
  (cond
    (keyword? url) (url-for url)
    (seqable? url) (apply url-for url)
    :else url))

(defn post-url
  [url & {:as options}]
  (let [headers (request-headers-from options)
        body (request-body-from options)
        url  (url-from url)]
    (response-for service :post url :body body :headers headers)))

(defn get-url [url]
  (response-for service :get (url-from url)))
