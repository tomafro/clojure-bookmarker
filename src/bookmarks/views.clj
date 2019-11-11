(ns bookmarks.views
  (:require
   [clojure.java.io]
   [clostache.parser :as clostache]))

(defn view
  ([name] (view name {:name "Bill" :example (fn [text] (fn [render] (str "<b>" (render text) "</b>")))}))
  ([name context]
   (clostache/render-resource name context)))
