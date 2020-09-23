(ns eidolon.core
  (:require [com.rpl.specter :as sr]))

(def INDEXED
  "A path that visits v and collects k in [[k v], ...].

  This is useful if you want to collect a path to something, see [[path-walker]]
  and [[path-finder]]. "
  [sr/ALL (sr/collect-one sr/FIRST) sr/LAST])

(def INDEXED-SEQ
  "A selector that visits all elements of a seq, and collects their indices.

  This is useful if you want to collect a path to something, see [[path-walker]]
  and [[path-finder]]."
  [sr/INDEXED-VALS (sr/collect-one sr/FIRST) sr/LAST])

(def path-finder
  "Finds the first entry matching `pred` in a deeply nested structure of maps
  and vectors, and collects the path on the way there."
  (sr/recursive-path
   [term-pred] p
   (sr/cond-path
    (sr/pred term-pred) sr/STAY
    map? [INDEXED p]
    coll? [INDEXED-SEQ p])))

(def path-walker
  "Walks the first entry matching `pred` in a deeply nested structure of maps
  and vectors, and collects the path on the way there.

  This will do depth-first search. This is important, because if you use it with
  a transform, you may be modifying the tree in a way that invalidates the
  previously-collected path.

  Like [[path-finder]], but will recurse into matching entries. Hence, also
  takes a recurse path to tell it how to continue recursing on matches. For
  example, if you're matching a vector, you may want [[sr/ALL]]; if you're
  matching a map, you may want [[sr/MAP-VALS]]. Of course, those wouldn't
  collect that part of the path, so you might want [[INDEXED]]
  or [[INDEXED-SEQ]] instead."
  (sr/recursive-path
   [term-pred recurse-path] p
   (sr/cond-path
    (sr/pred term-pred) (sr/continue-then-stay recurse-path p)
    map? [INDEXED p]
    coll? [INDEXED-SEQ p])))

(def NESTED
  "A navigator for junctions in nested data structures."
  (sr/recursive-path
   [] p
   (sr/cond-path
    map? (sr/continue-then-stay sr/MAP-VALS p)
    coll? (sr/continue-then-stay sr/ALL p))))

(def NESTED-PATHS
  "Like [[NESTED]], but collects the path along the way."
  (sr/recursive-path
   [] p
   (sr/cond-path
    map? [INDEXED p]
    coll? [INDEXED-SEQ p]
    sr/STAY sr/STAY)))

(def TREE-KEYS
  "A navigator for all of the map keys in a nested tree."
  (sr/comp-paths NESTED map? sr/MAP-KEYS))

(def TREE-LEAVES
  "A navigator for all of the leaves in a nested tree."
  (sr/recursive-path
   [] p
   (sr/cond-path
    map? [sr/MAP-VALS p]
    coll? [sr/ALL p]
    sr/STAY sr/STAY)))
