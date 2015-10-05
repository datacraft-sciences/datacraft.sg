(ns datacraft.core
  (:use [org.httpkit.server :only [run-server]]
        [mikera.cljutils error]
        [clojure.repl])
  (:require [compojure.handler :as handler]
            [clout.core] 
            [compojure.route :as route]
            [compojure.core :as cc :refer [GET POST DELETE PUT ANY OPTIONS defroutes context]]
            [ring.util.response :as resp]
            [ring.middleware.cors :refer [wrap-cors]] 
            [ring.middleware.params]
            [clojure.java.jdbc :as j]
            [cheshire.core :as json]
            [hiccup.util :refer [escape-html]]
            [hiccup.core :refer [html]]
            [clojure.string]
            [clojure.java.io :as io])
  (:require [clojure.tools.logging :as log])
  (:require [selmer.parser :as selmer]) 
  (:require [org.httpkit.client :as http])
  (:gen-class))

(selmer.parser/set-resource-path! (clojure.java.io/resource "templates"))

(defroutes app-routes
  (GET "/index.html" params (selmer/render-file "index.html" params))
  (GET "/404.html" params (selmer/render-file "404.html" params))
  (GET "/solutions.html" params (selmer/render-file "solutions.html" params))
  (GET "/technology.html" params (selmer/render-file "technology.html" params))
  (GET "/why-datacraft.html" params (selmer/render-file "why-datacraft.html" params))
  (GET "/about-us.html" params (selmer/render-file "about-us.html" params))
  (GET "/meet-the-team.html" params (selmer/render-file "meet-the-team.html" params))
  (GET "/contact-us.html" params (selmer/render-file "contact-us.html" params))
  (GET "/error.html" params (error "fvodbhofvbofe"))
  
  (context "/info" []
    (GET "/" params "correct info path")
    (GET "/custom-resource-path" params (pr-str @selmer.util/*custom-resource-path*)))
  
  (route/resources "/")   ;; defaults to reading from /public path on classpath
  (route/files "/" {:root "~/public"})
  (route/files "/" {:root "/public"})
  
  (GET "/" [] (resp/redirect "/index.html"))

  (route/not-found "Page not found"))

(defn wrap-error [handler]
  (fn [request]
    (try (handler request)
      (catch Exception e
         (let [etext (stacktrace-str e)
               etext (clojure.string/replace etext "\n" "<br/>")]
           {:status 500
            :body (selmer/render-file "500.html" {:error-text [:safe etext]})})))))

(def app
  (-> app-routes 
      wrap-error
      ;; (wrap-cors :access-control-allow-origin #".*")
      (handler/site)))

(defn -main [& args] ;; entry point, lein run will enter from here
  (run-server #'app {:port 80}))