(ns logic-test.graph-test
  (:refer-clojure :exclude [==])
  (:require [logic-test.graph :as graph]
    [clojure.core.logic :as logic]
    [clojure.test :as test]))

(test/deftest graph
  (test/is
    (=
      '((:a :b :c :f) (:a :b :e :f))
      (logic/run* [q] (graph/path :a :f q)))))
