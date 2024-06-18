(ns todo-list.handler
  (:require [compojure.core :refer [defroutes GET POST PUT DELETE]]
            [compojure.route :as route]
            [hiccup2.core :as h]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.util.response :as response]))

(defroutes app-routes
  (GET "/foo" []{:status 200, :headers {"Content-Type" "text/html"}, :body (str (h/html [:span {:class "foo"} "bar"]))})
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes {:static {:resources "public"
                                      :files     "resources/public"}}))
