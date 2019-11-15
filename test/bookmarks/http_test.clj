(ns bookmarks.http-test
  (:require [clojure.test :refer [deftest testing is use-fixtures]]
            [bookmarks.http]
            [test.http :as http]
            [test.html :as html]
            [net.cgrand.enlive-html :refer [attr=]]
            [database]
            [bookmarks.db]))

(use-fixtures :each http/run-within-transaction)

(deftest new-bookmark-test
  (let [response (http/get-url :bookmarks/new)]
    (is (html/select? response "form"))))

; (deftest create-bookmark-test
;   (testing "successful create"
;     (let [response (http/post-url :bookmarks/create :form #:bookmarks{:title "Blog" :url "https://tomafro.net"})]
;       (is (= 201 (:status response)))
;       (is (= "created" (:body response)))
;       (is (= #:bookmarks{:title "Blog" :url "https://tomafro.net"}
;              (select-keys (bookmarks.db/find-first) [:bookmarks/title :bookmarks/url]))))))
    
(deftest show-bookmark-test
  (testing "missing bookmark"
    (let [response (http/get "/bookmark/1234")]
      (is (= 404 (:status response)))))
  (testing "existing bookmark"
    (let [bookmark (bookmarks.db/create #:bookmarks{:title "Blog" :url "https://tomafro.net"})
          response (http/get-url [:bookmark :params {:bookmark-id (:bookmarks/id bookmark)}])]
      (is (= 200 (:status response)))
      (is (html/select? response "a[href=https://tomafro.net]:containsOwn(Blog)")))))

(deftest index-bookmarks-test
  (is (= "LIST" (:body (http/get "/bookmarks")))))
