(ns logic-test.ckanren-test
  (:refer-clojure :exclude [==])
  (:require [clojure.core.logic :as l]
            [clojure.test :as t]))

(t/deftest test-pair []
  (t/is (= (l/pair 1 2)
         (l/pair 1 2))))

(t/deftest test-domfd-1 []
  (let [x (l/lvar 'x)
        s ((l/domfd x 1) l/empty-s)]

    (t/is (= (:s s) {x 1}))))