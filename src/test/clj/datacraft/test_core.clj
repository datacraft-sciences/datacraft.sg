(ns datacraft.test-core
  (:use [clojure.test])
  (:use [datacraft.core]))

(deftest test-site
  (let [app datacraft.core/app]
    ;;(is (app {}))
    ))

