(ns eidolon.core-test
  (:require [clojure.test :as t]
            [eidolon.core :as e]
            [com.rpl.specter :as sr]))

(def finder-sample-haystack
  {:a {:b {:c ['needle {:d ['second-needle]}]}
       :x :nothing
       :y :even-less}})

(t/deftest path-finder-walker-tests
  (t/is (= [[:a :b :c ['needle {:d ['second-needle]}]]]
           (sr/select (e/path-finder vector?) finder-sample-haystack))
        "path-finder finds the first needle, doesn't recurse")
  (t/is (= [[:a :b :c 1 :d ['second-needle]]
            [:a :b :c ['needle {:d ['second-needle]}]]]
           (sr/select (e/path-walker vector? e/INDEXED-SEQ) finder-sample-haystack))
        "path-walker finds the deepest needle first, then the shallow needle"))

(t/deftest indexed-tests
  (t/is (= [[:a :b] [:c [:d]]]
           (sr/select [e/INDEXED] {:a :b :c [:d]}))))

(t/deftest indexed-seq-tests
  (let [xs [:a :b :c :d]]
    (t/is (= (map-indexed vector xs)
             (sr/select [e/INDEXED-SEQ] xs)))))

(def simple-tree
  {:a 1})

(def tree-with-strings-in-it
  {:a "Cooking MC's like a pound of bacon"})

(def nested-tree
  {:a {:b {:c 0
           :d 1}
       :e 2
       :f #{3 4}
       :g {:h 5 :i 6}}})

(def nested-colls-tree
  "A tree with values nested inside seqable things (sets, vecs...)"
  {:a
   [0 1
    {:b
     {:c 2
      :d #{3 {:e 4
              :f '(5 {:g 6})}}}}]})

(t/deftest tree-keys-tests
  (t/is (= [:a] (sr/select e/TREE-KEYS simple-tree)))
  (t/is (= [:a] (sr/select e/TREE-KEYS tree-with-strings-in-it)))
  (t/is (= #{:a :b :c :d :e :f :g :h :i}
           (set (sr/select e/TREE-KEYS nested-tree))))
  (t/is (= #{:a :b :c :d :e :f :g}
           (set (sr/select e/TREE-KEYS nested-colls-tree)))))

(t/deftest tree-leaves-tests
  (t/is (= [1]
           (sr/select e/TREE-LEAVES simple-tree)))
  (t/is (= ["Cooking MC's like a pound of bacon"]
           (sr/select e/TREE-LEAVES tree-with-strings-in-it)))
  (t/is (= (set (range 7))
           (set (sr/select e/TREE-LEAVES nested-tree))))
  (t/is (= (set (range 7))
           (set (sr/select e/TREE-LEAVES nested-colls-tree)))))
