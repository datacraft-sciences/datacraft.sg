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
  (route/resources "/")   ;; defaults to reading from /public path on classpath
  (route/files "/" {:root "~/public"})
  (route/files "/" {:root "/public"})
  
  (GET "/" [] (resp/redirect "/index.html"))

  (ANY "/:path" [] (resp/redirect "/404.html")))

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