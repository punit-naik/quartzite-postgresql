(ns org.clojars.punit-naik.quartzite-postgresql.MyDynamicClassLoader
  (:gen-class
   :extends clojure.lang.DynamicClassLoader
   :implements [org.quartz.spi.ClassLoadHelper]
   :exposes-methods {loadClass parentLoadClass}))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn -initialize
  #_{:clj-kondo/ignore [:unused-binding]}
  [this]
  nil)

(defn load-class
  "This is named with a dashified format to avoid a StackOverflowError by
   endlessly calling loadClass of _this_ class and not the parent."
  [loader name]
  (.parentLoadClass loader name true))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn -loadClass
  ([this name]
   (load-class this name))
  ([this name _]
   (load-class this name)))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn -getClassLoader [this]
  this)