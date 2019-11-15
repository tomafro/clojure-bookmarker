(ns echo-test
  (:require [clojure.test :refer [deftest testing is use-fixtures]]
            [bookmarks.http]
            [test.http :as http]
            [test.html :as html]
            [database]
            [bookmarks.db]))

(use-fixtures :each http/run-within-transaction)

(deftest ^:focus echo-test
  (let [response (http/get "/echo")]
    (is (= 200 (:status response)))))
