(ns logic-test.hanoi-test
  (:refer-clojure :exclude [==])
  (:use [clojure.test :only [deftest is]])
  (:require [clojure.core.logic :as l]
            [logic-test.hanoi :as hanoi]))
  
(deftest hanoi
  (is
    (= 
      '(_0)
      (l/run* [q]
        (hanoi/moveo 3 :left :right :center)))))
