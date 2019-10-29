(ns bookmarks.http
  (:require
   [bookmarks.views]
   [database]
   [response]
   [bookmarks.db]
   [io.pedestal.interceptor.helpers :as interceptor]))

(defn new-bookmark
  [request]
  (response/ok (bookmarks.views/new-bookmark)))

(defn show-bookmark
  [request]
  (if-let [bookmark (get-in request [:bookmarker :bookmark])]
      (response/ok (str "<a href=\"" (:bookmarks/url bookmark) "\">" (:bookmarks/title bookmark) "</a>"))
      (response/not-found "Not found")))

(defn create-bookmark
  [request]
  (bookmarks.db/create #:bookmarks{:url "https://tomafro.net" :title "Title"})
  (response/created "created"))

(defn index-bookmarks
  [request]
  (response/ok "LIST"))

(def find-bookmark
  (interceptor/on-request
   (fn [request]
     (let [bookmark-id (get-in request [:path-params :bookmark-id])]
       (assoc-in request [:bookmarker :bookmark]
                 (bookmarks.db/find (Integer/parseInt bookmark-id)))))))

(defn routes
  []
  [["/bookmarks" {:get [:bookmarks `index-bookmarks] :post [:bookmarks/create `create-bookmark]}
     ["/new" {:get [:bookmarks/new `new-bookmark]}]]
    ["/bookmark/:bookmark-id" ^:interceptors [find-bookmark] {:get [:bookmark `show-bookmark]}]])
