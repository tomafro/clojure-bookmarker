(ns test.html
  (:refer-clojure :exclude [get select])
  (:require [net.cgrand.enlive-html :as html]))

(defn select [response selector]
  (let [html (html/html-resource (java.io.StringReader. (:body response)))]
    (html/select html selector)))
