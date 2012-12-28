(ns logic-test.match-test
  (:refer-clojure :exclude [==])
  (:require [clojure.core.logic :as l]
            [clojure.test :as t]))

(l/defne pm1 [x y]
  ([:foo :bar]))

(l/defne pm2 [x y]
  ([_ x]))

(l/defne pm3 [x y]
  ([_ 'x]))

(l/defne pm4 [x y]
  ([[h . t] t]))

(t/deftest test-pm []
  (t/is (= (l/run* [q] (l/fresh [x y] (l/== q [x y]) (pm1 x y))) '([:foo :bar])))
  (t/is (= (l/run* [q] (l/fresh [x y] (pm2 x y) (l/== x y))) '(_0)))
  (t/is (= (l/run* [q] (pm4 '(1 2) q)) '((2)))))

#_(l/defne form->ast1 [form ast]
  (['(fn ~args . ~body) {:op :fn :args args :body body}]))

#_(l/defne form->ast2 [form ast]
  (['(fn [~f . ~rest] . ~body) {:op :fn :f f :rest rest :body body}]))

#_(t/deftest test-code-match-1
  (t/is (= (l/run* [q]
           (form->ast1 '(fn [x y] (+ x y)) q))
         '({:op :fn :args [x y] :body ((+ x y))})))
  (t/is (= (l/run* [q]
           (form->ast2 '(fn [x y] (+ x y)) q))
         '({:op :fn :f x :rest (y) :body ((+ x y))}))))