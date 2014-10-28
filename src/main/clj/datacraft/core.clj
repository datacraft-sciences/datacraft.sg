(ns datacraft.core
  (:use [org.httpkit.server :only [run-server]]
        [mikera.cljutils error]
        [clojure.repl])
  (:require [compojure.handler :as handler]
            [clout.core] 
            [compojure.route :as route]
            [compojure.core :as cc :refer [GET POST DELETE PUT ANY OPTIONS defroutes]]
            [ring.util.response :as resp]
            [ring.middleware.cors :refer [wrap-cors]] 
            [ring.middleware.params]
            [clojure.java.jdbc :as j]
            [cheshire.core :as json]
            [clojure.string]
            [clojure.java.io :as io])
  (:require [clojure.tools.logging :as log])
  (:require [selmer.parser :as selmer]) 
  (:require [org.httpkit.client :as http])
  (:gen-class))

(selmer.parser/set-resource-path! (clojure.java.io/resource "templates"))

(defroutes app-routes
  (GET "/index.html" params (selmer/render-file "index.html" params))
  (GET "/solutions.html" params (selmer/render-file "solutions.html" params))
  (GET "/technology.html" params (selmer/render-file "technology.html" params))
  (GET "/why-datacraft.html" params (selmer/render-file "why-datacraft.html" params))
  (GET "/about-us.html" params (selmer/render-file "about-us.html" params))
  (GET "/contact-us.html" params (selmer/render-file "contact-us.html" params))
  
  (route/resources "/")   ;; defaults to reading from /public path on classpath
  (route/files "/" {:root "~/public"})
  (route/files "/" {:root "/public"})
  
  (GET "/" [] (resp/redirect "/index.html"))

  (route/not-found "<p>404 - Page Not Found.</p> 
                    <p>Perhaps you want the <a href=\"\\index.html\">index.html</a>?</p>"))

(def app
  (-> app-routes 
      ;; (wrap-cors :access-control-allow-origin #".*")
      (handler/site)))

(defn -main [& args] ;; entry point, lein run will enter from here
  (run-server #'app {:port 80}))