(ns bookmarks.views
  (:require
   [clojure.java.io :as io]
   [clostache.parser :as clostache]))

(defn render [view context partials]
  (clostache/render-resource (str view ".mustache") context partials))

(defn read-view [name]
  (slurp (io/resource (str "views/" name ".mustache"))))

(defn view
  ([name] (view name {:name "Bill" :example (fn [text] (fn [render] (str "<b>" (render text) "</b>")))}))
  ([name context]
    (clostache/render-resource "layouts/main.mustache" context {:content (read-view name)})))
