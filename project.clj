(defproject datacraft "0.0.2-SNAPSHOT"
  :description "FIXME: write this!"
  :url "http://example.com/FIXME"

  :dependencies [[org.clojure/clojure "1.8.0-alpha5"]
                 [net.mikera/trellis "0.0.6" :exclusions [org.clojure/clojure]]
                 [net.mikera/timeline "0.4.0"]
                 [net.mikera/clojure-utils "0.6.2"]
                 [selmer "0.9.2"]
                 [log4j/log4j "1.2.17" :exclusions [javax.mail/mail
                                              javax.jms/jms
                                              com.sun.jdmk/jmxtools
                                              com.sun.jmx/jmxri]]]

  :plugins [[lein-cljsbuild "1.0.0"]
            [lein-ring "0.8.7"]]

  :ring {:handler datacraft.core/app
         :init    datacraft.core/init}

  :source-paths ["src/main/clj"]
  :test-paths ["src/test/clj"]
  
  :resource-paths ["resources"]
  
  :main datacraft.core

  :cljsbuild {
              :builds [{:id "dev"
                        :source-paths ["src/main/cljs"]
                        :compiler {
                                   :output-to "resources/public/js/app.js"
                                   :output-dir "resources/public/js/out"
                                   :optimizations :none
                                   :source-map true
                                   :externs ["om/externs/react.js"]}}
                       {:id "release"
                        :source-paths ["src/main/cljs"]
                        :compiler {
                                   :output-to "resources/public/js/app.js"
                                   :source-map "resources/public/js/app.js.map"
                                   :optimizations :advanced
                                   :pretty-print false
                                   :output-wrapper false
                                   :preamble ["om/react.min.js"]
                                   :externs ["om/externs/react.js"]
                                   :closure-warnings
                                   {:non-standard-jsdoc :off}}}]})
