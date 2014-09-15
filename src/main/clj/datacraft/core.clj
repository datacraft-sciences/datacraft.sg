(ns datacraft.core
  (:use [org.httpkit.server :only [run-server]]
        [mikera.cljutils error]
        [clojure.repl])
  (:require [compojure.handler :as handler]
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
  (:require [org.httpkit.client :as http]))

(defroutes app-routes
  (route/resources "/")   ;; defaults to reading from /public path on classpath
  (route/files "/" {:root "~/public"})
  (route/files "/" {:root "/public"})
  
  (GET "/" [] (resp/redirect "/index.html"))

  (route/not-found "<p>404 - Page Not Found.</p> 
                    <p>Perhaps you want the <a href=\"\\index.html\">index.html</a>?</p>"))

(def app
  (-> #'app-routes 
      ;; (wrap-cors :access-control-allow-origin #".*")
      (handler/site)))

(defn -main [& args] ;; entry point, lein run will enter from here
  (run-server app {:port 80}))