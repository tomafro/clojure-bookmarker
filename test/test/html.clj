(ns test.html
  (:refer-clojure :exclude [get select])
  (:import (org.jsoup Jsoup) 
  (org.jsoup.select Elements)
  (org.jsoup.nodes Element)))

(defn response->page [response]
  (Jsoup/parse (:body response)))

(defn select [response selector]
  (.select (response->page response) selector))

(defn select? [response selector]
  (> (count (select response selector)) 0))
